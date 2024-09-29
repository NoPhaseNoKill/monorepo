package com.nophasenokill.domain

import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File


class IncrementalTestTaskTest : PluginTest() {

    lateinit var cowClass: File
    lateinit var cowTestClass: File

    private val initialSourceFile256SHA = "f653d7579ee0253c63b95503bb4a0e3ec54468dcd866645cb29c192bf2d35c92"
    private val initialTestFile256SHA = "48f76f0ebeeb6590f783aecbbfe0069c82e2c40ab62ed441a05a921a696b226a"
    private val modifiedSourceFile256SHA = "83ca07f6137c6e34e09521701974c098dcadf52fd94f6a4ac60090fe692b20fe"
    private val modifiedTestFile256SHA = "6fa562aa24e1419a2a831592ad3b37afb956da5ba250d651185ffe41e7db061e"
    private val animalSourceFile256SHA = "422d7462428ae4e01e933450020193692cd9cc0ad1bf219e40cdf7a3b8a060cb"
    private val animalSourceFileWithLongQualifier256SHA = "187cf33a8f748f74e7378a6d967ad40ef1be8834937fa89e53aafab2aa83ba69"

    @BeforeEach
    fun setup() {
        setupBuildFile()
        buildDir.deleteRecursively()
        sourceDir.deleteRecursively()
    }

    @Test
    fun `should calculate initial digest value, and modify value after that correctly when using same files`() {
        val initialValue = 0
        val modifiedValue = 1

        cowClass = createInitialCowClass(initialValue)
        cowTestClass = createInitialCowTestClass(initialValue)

        runAndAssertSuccess("computeClassDigests")

        val digest = buildDir.resolve("computeClassDigests.txt")

        val initialDigestResult = digest.readText()
        val initialExpected = """
            example.CowTest: $initialTestFile256SHA
            example.Cow: $initialSourceFile256SHA
        """.trimIndent()

        assertEquals(initialExpected, initialDigestResult)

        modifyCowTestAssertion(modifiedValue)
        modifyCowFunctionMooReturnValue(modifiedValue)

        runAndAssertSuccess("computeClassDigests")

        val digestAfter = buildDir.resolve("computeClassDigests.txt")
        val modifiedDigestResult = digestAfter.readText()

        val modifiedExpected = """
            example.CowTest: $modifiedTestFile256SHA
            example.Cow: $modifiedSourceFile256SHA
        """.trimIndent()

        assertEquals(modifiedExpected, modifiedDigestResult)
    }

    @Test
    fun `should add additional digest to secondary run when adding source after first run`() {
        val initialValue = 0
        val modifiedValue = 1

        cowClass = createInitialCowClass(initialValue)
        cowTestClass = createInitialCowTestClass(initialValue)

        runAndAssertSuccess("computeClassDigests")

        val digest = buildDir.resolve("computeClassDigests.txt")

        val initialDigestResult = digest.readText()
        val initialExpected = """
            example.CowTest: $initialTestFile256SHA
            example.Cow: $initialSourceFile256SHA
        """.trimIndent()

        assertEquals(initialExpected, initialDigestResult)

        modifyCowTestAssertion(modifiedValue)
        modifyCowFunctionMooReturnValue(modifiedValue)
        createInitialAnimalInterface()

        runAndAssertSuccess("computeClassDigests")

        val digestAfter = buildDir.resolve("computeClassDigests.txt")
        val modifiedDigestResult = digestAfter.readText()

        val modifiedExpected = """
            example.CowTest: $modifiedTestFile256SHA
            example.Cow: $modifiedSourceFile256SHA
            example.Animal: $animalSourceFile256SHA
        """.trimIndent()


        assertEquals(modifiedExpected, modifiedDigestResult)
    }

    @Test
    fun `should remove digest if file is in first run, but not secondary`() {
        val initialValue = 0
        val modifiedValue = 1

        cowClass = createInitialCowClass(initialValue)
        cowTestClass = createInitialCowTestClass(initialValue)
        val animalClass = createInitialAnimalInterface()

        runAndAssertSuccess("computeClassDigests")

        val digest = buildDir.resolve("computeClassDigests.txt")

        val initialDigestResult = digest.readText()
        val initialExpected = """
            example.CowTest: $initialTestFile256SHA
            example.Cow: $initialSourceFile256SHA
            example.Animal: $animalSourceFile256SHA
        """.trimIndent()

        assertEquals(initialExpected, initialDigestResult)

        modifyCowTestAssertion(modifiedValue)
        modifyCowFunctionMooReturnValue(modifiedValue)
        animalClass.delete()

        runAndAssertSuccess("computeClassDigests")

        val digestAfter = buildDir.resolve("computeClassDigests.txt")
        val modifiedDigestResult = digestAfter.readText()

        val modifiedExpected = """
            example.CowTest: $modifiedTestFile256SHA
            example.Cow: $modifiedSourceFile256SHA
        """.trimIndent()


        assertEquals(modifiedExpected, modifiedDigestResult)
    }

    @Test
    fun `should add qualified name to digest`() {
        val initialValue = 0

        cowClass = createInitialCowClass(initialValue)
        cowTestClass = createInitialCowTestClass(initialValue)
        createInitialAnimalInterface("src/main/kotlin/some/really/long/qualified/name")

        runAndAssertSuccess("computeClassDigests")

        val digest = buildDir.resolve("computeClassDigests.txt")

        val initialDigestResult = digest.readText()
        val initialExpected = """
            example.CowTest: $initialTestFile256SHA
            example.Cow: $initialSourceFile256SHA
            some.really.long.qualified.name.Animal: $animalSourceFileWithLongQualifier256SHA
        """.trimIndent()

        assertEquals(initialExpected, initialDigestResult)
    }


        @Test
    fun `test incremental test invalidation flow`() {
        val initialValue = 0
        val modifiedValue = 1

        testCodeInvalidation(
            taskName = "incrementalTest",
            initialValue = initialValue,
            modifiedValue = modifiedValue
        )
    }

    @Test
    fun `test normal test invalidation flow`() {
        val initialValue = 0
        val modifiedValue = 1

        testCodeInvalidation(
            taskName = "test",
            initialValue = initialValue,
            modifiedValue = modifiedValue
        )
    }

    private fun testCodeInvalidation(
        taskName: String,
        initialValue: Int,
        modifiedValue: Int
    ) {
        /*
            1. Run task -> Success
            2. Run again -> Up to date
            3. Modify test assertion -> Test should fail
            4. Fix code being tested -> Test should pass (success)
            5. Run again -> Up to date
         */

        cowClass = createInitialCowClass(initialValue)
        cowTestClass = createInitialCowTestClass(initialValue)
        runAndAssertSuccess(taskName)
        runAndAssertUpToDate(taskName)

        modifyTestCodeToFail(taskName, initialValue, modifiedValue)
        modifySourceCodeToFix(taskName, modifiedValue)

        runAndAssertUpToDate(taskName)
    }

    private fun modifySourceCodeToFix(taskName: String, value: Int) {
        modifyCowFunctionMooReturnValue(value)

        val result4 = runTask(taskName)

        val expectedOutputToContain = when (taskName) {
            "test" -> {
                """
                        > Task :$taskName
            
                        CowTest STARTED
                        
                        CowTest > testMoo() STARTED
                        
                        CowTest > testMoo() PASSED
                        
                        CowTest PASSED
                    """.trimIndent()
            }

            "incrementalTest" -> {
                """
                    > Task :computeClassDigests
                    Computed digests for class files: {example.CowTest=${modifiedTestFile256SHA}, example.Cow=${modifiedSourceFile256SHA}}
                    
                    > Task :mapTestToClassDigests_v2
                    Starting mapTestToClassDigests task
                    
                    > Task :testClasses UP-TO-DATE
                    
                    > Task :${taskName}
                    
                    CowTest STARTED
                    
                    CowTest > testMoo() STARTED
                    
                    CowTest > testMoo() PASSED
                    
                    CowTest PASSED
                    
                    BUILD SUCCESSFUL
                    """.trimIndent()
            }

            else -> {
                throw Exception("Unknown task type")
            }
        }

        assertEquals(SUCCESS, result4.task(":$taskName")?.outcome)
        println("OUTPUT WAS: ${result4.output}")
        assertTrue(result4.output.contains(expectedOutputToContain))
    }

    private fun modifyTestCodeToFail(taskName: String, initialValue: Int, modifiedValue: Int) {
        modifyCowTestAssertion(modifiedValue)

        val result3 = runTaskWithFailure(taskName)

        assertTrue(result3.output.contains("expected: <$modifiedValue> but was: <$initialValue>"))
        assertEquals(TaskOutcome.FAILED, result3.task(":$taskName")?.outcome)
    }

    private fun runAndAssertSuccess(taskName: String) {
        val result = runTask(taskName)
        assertEquals(SUCCESS, result.task(":$taskName")?.outcome)
    }

    private fun runAndAssertUpToDate(taskName: String) {
        val result = runTask(taskName)
        assertEquals(TaskOutcome.UP_TO_DATE, result.task(":$taskName")?.outcome)
    }

    private fun modifyCowFunctionMooReturnValue(value: Int) {
        cowClass.writeText(
            """
                        package example
            
                        class Cow {
                            fun moo(): Int = $value
                        }
                    """.trimIndent()
        )
    }

    private fun modifyCowTestAssertion(value: Int) {
        cowTestClass.writeText(
            """
                    package example
    
                    import org.junit.jupiter.api.Test
                    import org.junit.jupiter.api.Assertions.assertEquals
    
                    class CowTest {
    
                        @Test
                        fun testMoo() {
                            val cow = Cow()
                            assertEquals($value, cow.moo()) // Force failure here
                        }
                    }
                    """.trimIndent()
        )
    }

    private fun createInitialCowTestClass(value: Int): File {
        val cowTestClass = File(testProjectFile, "src/test/kotlin/example/CowTest.kt")

        cowTestClass.parentFile.mkdirs()

        cowTestClass.writeText(
            """
                    package example
    
                    import org.junit.jupiter.api.Test
                    import org.junit.jupiter.api.Assertions.assertEquals
    
                    class CowTest {
    
                        @Test
                        fun testMoo() {
                            val cow = Cow()
                            assertEquals($value, cow.moo())
                        }
                    }
                    """.trimIndent()
        )
        return cowTestClass
    }

    private fun createInitialAnimalInterface(basePath: String = "src/main/kotlin/example"): File {
        val animalClass = File(testProjectFile, "$basePath/Animal.kt")
        animalClass.parentFile.mkdirs()
        val packagePath = basePath.substringAfter("src/main/kotlin/").replace("/", ".")

        animalClass.writeText(
            """
                    package $packagePath
    
                    interface Animal {
                        fun moo(): Int
                    }
                    """.trimIndent()
        )
        return animalClass
    }

    private fun createInitialCowClass(value: Int): File {
        val cowClass = File(testProjectFile, "src/main/kotlin/example/Cow.kt")
        cowClass.parentFile.mkdirs()

        cowClass.writeText(
            """
                    package example
    
                    class Cow {
                        fun moo(): Int = $value
                    }
                    """.trimIndent()
        )
        return cowClass
    }

    private fun setupBuildFile() {
        buildFile.writeText("") // clear any previous state from other tests

        buildFile.appendText(
            """
                import org.gradle.api.tasks.testing.logging.TestLogEvent;
                import org.gradle.api.tasks.testing.Test
                import org.gradle.api.tasks.testing.logging.TestExceptionFormat;
                
                plugins {
                        kotlin("jvm")
                        id("com.nophasenokill.incremental-test-plugin")
                }
                        
                repositories {
                    mavenCentral()
                }
   
                dependencies {
   
                   /*
                    Equivalent of: implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:2.0.20")
                 */
   
                    implementation(kotlin("gradle-plugin", "2.0.20"))
   
   
                    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
   
                    /*
                        These are required, so we don't implicitly load test framework. https://docs.gradle.org/8.7/userguide/upgrading_version_8.html#test_framework_implementation_dependencies
                     */
                    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
                    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.1")
                }
   
                tasks.withType(Test::class.java).configureEach {
                    useJUnitPlatform()
                    testLogging.events = setOf(
                        TestLogEvent.STANDARD_OUT,
                        TestLogEvent.STARTED,
                        TestLogEvent.PASSED,
                        TestLogEvent.SKIPPED,
                        TestLogEvent.FAILED,
                        TestLogEvent.STANDARD_OUT,
                        TestLogEvent.STANDARD_ERROR,
                    )
                    testLogging.showStackTraces = true
                    testLogging.exceptionFormat = TestExceptionFormat.FULL
                    testLogging.showStandardStreams
                    testLogging.minGranularity = 2
                }
                    
            """
        )

    }
}
