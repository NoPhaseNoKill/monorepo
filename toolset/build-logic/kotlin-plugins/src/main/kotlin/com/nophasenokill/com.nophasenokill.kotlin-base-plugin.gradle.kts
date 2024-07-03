import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.CompileUsingKotlinDaemon
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilerExecutionStrategy
import java.nio.file.Paths
import java.security.MessageDigest

plugins {
    kotlin("jvm")
}

afterEvaluate {

    group = "com.nophasenokill"
    version = "0.1.local-dev"

    kotlin {

        /*
            Equivalent of:

                java {
                    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
                }
         */
        jvmToolchain(21)

        /*
            Should be the same as gradle.properties jvmArgs

            -Xmx2g -XX:MaxMetaspaceSize=384m -Dfile.encoding=UTF-8 -XX:+HeapDumpOnOutOfMemoryError
         */
        kotlinDaemonJvmArgs = listOf("-Xmx2g", "-XX:MaxMetaspaceSize=384m", "-Dfile.encoding=UTF-8", "-XX:+HeapDumpOnOutOfMemoryError")
    }


    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
        jvmTargetValidationMode.set(org.jetbrains.kotlin.gradle.dsl.jvm.JvmTargetValidationMode.WARNING)
    }

    /*

        Defines where the Kotlin compiler is executed and if incremental compilation is supported in each case.

        Setting this to 'DAEMON' means:
            - It's executed in its own daemon process
            - Incremental compilation is supported
            - The default and fastest strategy. Can be shared between different Gradle daemons and multiple parallel compilations.

        See: https://kotlinlang.org/docs/gradle-compilation-and-caches.html#defining-kotlin-compiler-execution-strategy
     */
    tasks.withType<CompileUsingKotlinDaemon>().configureEach {
        compilerExecutionStrategy.set(KotlinCompilerExecutionStrategy.DAEMON)
    }

    /*
        A silent fallback to another strategy can consume a lot of system resources or lead to non-deterministic builds

        See: https://kotlinlang.org/docs/gradle-compilation-and-caches.html#kotlin-compiler-fallback-strategy
     */

    tasks.withType<KotlinCompile>().configureEach {
        useDaemonFallbackStrategy.set(false)
    }

    val versionCatalog = extensions.findByType(VersionCatalogsExtension::class.java)?.named("libs")
    val junitVersion = requireNotNull(versionCatalog?.findVersion("junit")?.get())
    val junitPlatformVersion = requireNotNull(versionCatalog?.findVersion("junitPlatform")?.get())

    dependencies {
        implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:${embeddedKotlinVersion}")
        implementation("org.jetbrains.kotlin:kotlin-stdlib:$embeddedKotlinVersion")

        testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")

        /*
            These are required so we don't implicitly load test framework. https://docs.gradle.org/8.7/userguide/upgrading_version_8.html#test_framework_implementation_dependencies
         */
        testImplementation("org.junit.jupiter:junit-jupiter:${junitVersion}")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher:${junitPlatformVersion}")
    }

    val testTasks = tasks.withType(Test::class.java)

    testTasks.configureEach {
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

        fileTree("src/main/kotlin").forEach { file ->
            logger.quiet("Input for src/main/kotlin is: ${file}")
        }

        fileTree("build/test-results").forEach { file ->
            logger.quiet("Input for build/test-results is: ${file}")
        }


        inputs.files("src/main/kotlin")
        outputs.dir("build/test-results")

    }
}

fun hashTestDirectoryContents(outputFileName: String, outputDir: File): File {
    val contents =  project.files(project.layout.projectDirectory.dir("test"))
    val hashMethod = "MD5"

    var md5 = ""
    val md = MessageDigest.getInstance(hashMethod)

    logger.quiet("The directories being included in DirHashTask are: ${contents.map { it.path }}")

    contents.forEach { directory ->

        directory.walkTopDown()
            .filter { it.isFile }
            .forEach {
                val digest = md.digest(it.readBytes())
                md5 += BigInteger(1, digest).toString(16)
            }
    }

    outputDir.mkdirs()
    val hashFile = Paths.get(outputDir.absolutePath, outputFileName)
    val digest = md.digest(md5.toByteArray())
    val file = File(hashFile.toUri())
    file.writeText(BigInteger(1, digest).toString(16))
    return hashFile.toFile()
}

/*
    Awful way of creating hashes, but gets the job done for demonstration purposes.

    Creates two files in the build dir: current-has.txt and previous-hash.txt
 */
val getCurrentAndPreviousHash = tasks.register("getCurrentAndPreviousHash") {

    val testClassDir = project.layout.projectDirectory.dir("test") // used in hashTestDirectoryContents
    val buildDir = project.layout.buildDirectory
    val currentHashPath = buildDir.asFile.get().resolve("current-hash.txt")
    val previousHashPath =  buildDir.asFile.get().resolve("previous-hash.txt")
    val hashesDir = project.layout.buildDirectory.dir("test-classes-hash").get().asFile
    val tempOutputFile = hashTestDirectoryContents("current-hash.txt", hashesDir)

    // inputs.files(testClassDir)
    outputs.files(currentHashPath, previousHashPath, tempOutputFile)

    doLast {

        if(previousHashPath.exists()) {
            if(previousHashPath.readLines().isEmpty()) {
                logger.quiet("First time comparing test classes. Previous hash doesnt exist")
            } else if (tempOutputFile.readLines() == previousHashPath.readLines()) {
                logger.quiet("Test class hashes are the same. Test classes have not changed")
            } else {
                logger.quiet("Test class hashes are NOT the same. Test classes HAVE changed")
            }

            tempOutputFile.copyTo(previousHashPath, overwrite = true)
            hashesDir.deleteRecursively()

        } else {

            if (currentHashPath.exists()) {
                currentHashPath.copyTo(previousHashPath, overwrite = true)
                currentHashPath.delete()
                currentHashPath.createNewFile()
            }

            tempOutputFile.copyTo(currentHashPath, overwrite = true)
            hashesDir.deleteRecursively()
            previousHashPath.createNewFile()
            buildDir.get().asFile.resolve("test-classes-hash").delete()
        }
    }


}

/*
    Static way of checking/implementing a way to determine
    whether the newly compiled files should trigger a test re-execution.

    1. If test class hashes have changed, rely on the default gradle behaviour
    2. If they haven't changed, we should see whether the underlying execution classes would trigger
        a change in our execution output
    3. If we expect change in our execution output -> rely on the default gradle behaviour
    4. Otherwise we don't need to execute tests

    To be able to ACTUALLY test this, you would need to test ABI compatibility across test classes.
    This is for example purposes only.
 */
val shouldExecuteTests = tasks.register("shouldExecuteTests") {
    dependsOn(getCurrentAndPreviousHash)

    val buildDir = project.layout.buildDirectory.get()
    val previous = buildDir.asFile.resolve("previous-hash.txt")
    val current = buildDir.asFile.resolve("current-hash.txt")
    val outputFile = buildDir.asFile.resolve("should-we-execute-test.txt")

    inputs.files(previous, current)
    outputs.files(outputFile)

    doLast {
        val hasNothingToCompareTo = buildDir.asFile.resolve("previous-hash.txt").readLines().isEmpty()
        val hashesAreDifferent = buildDir.asFile.resolve("current-hash.txt").readLines() !=
                buildDir.asFile.resolve("previous-hash.txt").readLines()

        val shouldReExecute = hasNothingToCompareTo || hashesAreDifferent

        logger.quiet("Should we re-execute tests? $shouldReExecute")
        /*
            We know for this example that if the tests haven't changed, because we're only
            uncommenting/re-commenting the multiply function, that this means test execution
            output/file shouldn't change
         */

        outputFile.writeText("should re-execute tests: $shouldReExecute")
    }

}

tasks.test {
    dependsOn(shouldExecuteTests)
    val shouldWeExecuteFile = project.layout.buildDirectory.get().asFile

    onlyIf {
        shouldWeExecuteFile.resolve("should-we-execute-test.txt").readLines().first() == "should re-execute tests: true"
    }
}
