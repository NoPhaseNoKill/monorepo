package com.nophasenokill.setup.junit.extensions

import TestDirectory
import com.nophasenokill.setup.runner.SharedRunnerDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.extension.*
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource
import org.junit.jupiter.api.io.CleanupMode
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference
import kotlin.io.path.Path
import kotlin.io.path.createFile

enum class GradleCreationState {
    INITIAL,
    CREATING_RUNNER,
    FULLY_OPERATIONAL
}

class GradleRunnerExtension: BeforeAllCallback, BeforeEachCallback, AfterAllCallback, ParameterResolver, CloseableResource {

    @field:TempDir(cleanup = CleanupMode.ON_SUCCESS)
    val sharedRunnerDir: Path = Files.createTempDirectory("shared-runner-dir")
    private val testClassTimes: ConcurrentHashMap<String, Long> = ConcurrentHashMap()
    private var name: String? = null

    override fun beforeAll(context: ExtensionContext) {

        val value = SharedTestSuiteStore.getRoot(context).get(SharedTestSuiteContextKey.TESTS_STARTED)
        if (value == null) {
            testClassTimes["${context.displayName} + start"] = System.currentTimeMillis()
            println("All times are now: $testClassTimes")

            SharedTestSuiteStore.putObjectIntoGlobalStore(
                context,
                SharedTestSuiteContextKey.INITIAL_GRADLE_RUNNER_BUILT,
                false
            )

            SharedTestSuiteStore.putObjectIntoGlobalStore(
                context,
                SharedTestSuiteContextKey.GRADLE_CREATION_STATE,
                GradleCreationState.INITIAL
            )

            SharedTestSuiteStore.putObjectIntoGlobalStore(
                context,
                SharedTestSuiteContextKey.TESTS_STARTED,
                this@GradleRunnerExtension
            )
            setupGlobalTestSuite(context)
        }
    }

    override fun beforeEach(context: ExtensionContext) {

        val ready= SharedTestSuiteStore.getRoot(context).get(SharedTestSuiteContextKey.INITIAL_GRADLE_RUNNER_BUILT) !== null

        println("Shared gradle runner details are ready: $ready")
    }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        return parameterContext.parameter.type == ExtensionContext::class.java
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any {
        return extensionContext
    }

    private fun setupGlobalTestSuite(context: ExtensionContext) = runTest {

            val creationState = SharedTestSuiteStore.getGradleCreationState(context)
            println("Creation state for: ${context.displayName} is $creationState")

            when(creationState) {
                GradleCreationState.INITIAL -> {
                    SharedTestSuiteStore.putObjectIntoGlobalStore(
                        context,
                        SharedTestSuiteContextKey.GRADLE_CREATION_STATE,
                        GradleCreationState.CREATING_RUNNER
                    )
                    val sharedRunnerDetails = createGradleRunner(context)

                    withContext(Dispatchers.IO) {
                        sharedRunnerDetails.gradleRunner.withArguments("build", "--warning-mode=all").build()
                    }

                    SharedTestSuiteStore.putObjectIntoGlobalStore(
                        context,
                        SharedTestSuiteContextKey.INITIAL_GRADLE_RUNNER_BUILT,
                        true
                    )
                    SharedTestSuiteStore.putObjectIntoGlobalStore(
                        context,
                        SharedTestSuiteContextKey.SHARED_GRADLE_RUNNER_DETAILS,
                        sharedRunnerDetails
                    )
                    SharedTestSuiteStore.putObjectIntoGlobalStore(
                        context,
                        SharedTestSuiteContextKey.GRADLE_CREATION_STATE,
                        GradleCreationState.FULLY_OPERATIONAL
                    )
                }
                GradleCreationState.CREATING_RUNNER -> {
                    // Poll every 50ms to see when runner becomes fully operational
                    while (SharedTestSuiteStore.getGradleCreationState(context) == GradleCreationState.CREATING_RUNNER) {
                        println("Polling..")
                        delay(50)
                    }
                }

                GradleCreationState.FULLY_OPERATIONAL -> {
                    // Do nothing
                }
            }

    }

    private suspend fun createGradleRunner(context: ExtensionContext): SharedRunnerDetails {
        return withContext(Dispatchers.IO) {
            val tempDir = Files.createDirectory(Path("$sharedRunnerDir/${context.displayName}-"))
            SharedTestSuiteStore.putObjectIntoGlobalStore(context, SharedTestSuiteContextKey.TEST_DIRECTORIES, TestDirectory(context.displayName, tempDir))

            val testDirectory =  SharedTestSuiteStore.getTestGradleDirectory(context, SharedTestSuiteContextKey.TEST_DIRECTORIES)
            val baseDir = testDirectory.mainDirectory
            val projectDir  = baseDir.resolve(context.displayName)
            projectDir.toFile().mkdirs()

            val buildFile = projectDir.resolve("build.gradle.kts")
            buildFile.createFile()
            val settingsFile = projectDir.resolve("settings.gradle.kts")

            val gradleRunner = SharedRunnerDetails.SharedRunner.getRunner(testDirectory)
                .withProjectDir(projectDir.toFile())
                .withPluginClasspath()

            return@withContext SharedRunnerDetails(
                projectDir.toFile(),
                buildFile.toFile(),
                settingsFile.toFile(),
                gradleRunner
            )
        }
    }

    override fun afterAll(context: ExtensionContext) {
        val endTime =  System.currentTimeMillis()
        testClassTimes["${context.displayName} + end"] = System.currentTimeMillis()
        println("End time of: ${context.displayName} was: $endTime")
        name = context.displayName
        println("All times are now: $testClassTimes for $name")
    }

    override fun close() {
        val startTimes = testClassTimes.filter { it.key.contains("start") }
        val finishTimes = testClassTimes.filter { it.key.contains("end") }

        val startTime = startTimes.map { it.value }.minOf { it }
        val finishTime = finishTimes.map { it.value }.maxOf { it }

        val timeTaken = finishTime - startTime
        println("Tests for class: $name took total wall clock time of: $timeTaken ms. Start time was: ${startTime}, end time was: $finishTime")
    }
}