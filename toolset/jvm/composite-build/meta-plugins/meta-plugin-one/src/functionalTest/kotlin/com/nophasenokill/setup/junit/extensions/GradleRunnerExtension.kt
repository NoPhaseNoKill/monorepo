package com.nophasenokill.setup.junit.extensions

import com.nophasenokill.setup.runner.SharedRunnerDetails
import com.nophasenokill.setup.variations.TestDirectory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.extension.*
import org.junit.jupiter.api.io.CleanupMode
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.createFile

class GradleRunnerExtension: BeforeAllCallback, BeforeEachCallback, ParameterResolver {

    @field:TempDir(cleanup = CleanupMode.ON_SUCCESS)
    val sharedRunnerDir: Path = Files.createTempDirectory("shared-runner-dir")

    override fun beforeAll(context: ExtensionContext) = runTest {

        val value = SharedTestSuiteStore.getRoot(context).get(SharedTestSuiteContextKey.TESTS_STARTED)
        if (value == null) {

            SharedTestSuiteStore.putObjectIntoGlobalStore(
                context,
                SharedTestSuiteContextKey.INITIAL_GRADLE_RUNNER_BUILT,
                false
            )

            SharedTestSuiteStore.putObjectIntoGlobalStore(
                context,
                SharedTestSuiteContextKey.TESTS_STARTED,
                this@GradleRunnerExtension
            )
            setupGlobalTestSuite(context)
        }
    }

    override fun beforeEach(context: ExtensionContext) = runTest {

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
        val sharedRunnerDetails = createGradleRunner(context)
        sharedRunnerDetails.gradleRunner.withArguments("build", "--warning-mode=all").build()
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
}