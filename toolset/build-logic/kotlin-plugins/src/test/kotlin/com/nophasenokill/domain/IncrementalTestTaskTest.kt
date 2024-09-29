package com.nophasenokill.domain

import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File


class IncrementalTestTaskTest : PluginTest() {


    @Test
    fun `test incremental testv2 invalidation flow`() {
        testNormalInvalidationByTaskName("incrementalTest")
    }

    @Test
    fun `test normal test invalidation flow`() {
        testNormalInvalidationByTaskName("test")
    }

    private fun testNormalInvalidationByTaskName(taskName: String) {
        val cowClass = File(testProjectFile, "src/main/kotlin/example/Cow.kt")
        val cowTestClass = File(testProjectFile, "src/test/kotlin/example/CowTest.kt")

        cowClass.parentFile.mkdirs()
        cowTestClass.parentFile.mkdirs()

        configure()

        cowClass.writeText(
            """
                package example

                class Cow {
                    fun moo(): Int = 0
                }
                """.trimIndent()
        )

        cowTestClass.writeText(
            """
                package example

                import org.junit.jupiter.api.Test
                import org.junit.jupiter.api.Assertions.assertEquals

                class CowTest {

                    @Test
                    fun testMoo() {
                        val cow = Cow()
                        assertEquals(0, cow.moo())
                    }
                }
                """.trimIndent()
        )

        // Run the test to generate the initial cache
        val result = runTask(taskName)

        assertEquals(SUCCESS, result.task(":$taskName")?.outcome)

        // Run the tests again to confirm all up to date
        val result2 = runTask(taskName)
        assertEquals(TaskOutcome.UP_TO_DATE, result2.task(":$taskName")?.outcome)

        // Modify CowTest to force a failure
        cowTestClass.writeText(
            """
                package example

                import org.junit.jupiter.api.Test
                import org.junit.jupiter.api.Assertions.assertEquals

                class CowTest {

                    @Test
                    fun testMoo() {
                        val cow = Cow()
                        assertEquals(1, cow.moo()) // Force failure here
                    }
                }
                """.trimIndent()
        )

        val result3 = runTaskWithFailure(taskName)

        assertTrue(result3.output.contains("expected: <1> but was: <0>"))
        assertEquals(TaskOutcome.FAILED, result3.task(":$taskName")?.outcome)

        cowClass.writeText(
            """
                    package example
        
                    class Cow {
                        fun moo(): Int = 1
                    }
                """.trimIndent()
        )

        val result4 = runTask(taskName)

        val expectedOutputToContain = """
            > Task :$taskName

            CowTest STARTED
            
            CowTest > testMoo() STARTED
            
            CowTest > testMoo() PASSED
            
            CowTest PASSED
        """.trimIndent()

        assertEquals(SUCCESS, result4.task(":$taskName")?.outcome)
        println("OUTPUT WAS: ${result4.output}")
        assertTrue(result4.output.contains(expectedOutputToContain))

        val result5 = runTask(taskName)

        assertEquals(TaskOutcome.UP_TO_DATE, result5.task(":$taskName")?.outcome)
    }

    private fun configure() {
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
