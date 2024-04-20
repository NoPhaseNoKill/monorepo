package com.nophasenokill.setup.variations

import com.nophasenokill.setup.junit.extensions.SharedTestSuiteContextKey
import com.nophasenokill.setup.junit.extensions.SharedTestSuiteStore
import com.nophasenokill.setup.runner.SharedRunnerDetails
import kotlinx.coroutines.runBlocking
import org.gradle.testkit.runner.BuildResult
import org.junit.jupiter.api.extension.ExtensionContext
import java.nio.file.Path
import kotlin.io.path.createFile

// @ExtendWith(SharedTestSuiteExtension::class, TestInvocationListener::class)
// @ExtendWith(SharedTestSuiteExtension::class)
// @ResourceLock("gradleFunctionalTest") // Required due to gradle file locking
open class FunctionalTest {

    fun runExpectedSuccessTask(details: SharedRunnerDetails, task: String): BuildResult  {
        return details.gradleRunner.withArguments(task, "--warning-mode=all").build()
    }

    fun createGradleRunner(context: ExtensionContext): SharedRunnerDetails  {

        val testDirectory =  SharedTestSuiteStore.getTestGradleDirectory(context, SharedTestSuiteContextKey.TEST_DIRECTORIES)
        val baseDir = testDirectory.mainDirectory
        val projectDir  = baseDir.resolve(context.displayName)
        projectDir.toFile().mkdirs()

        val buildFile = projectDir.resolve("build.gradle.kts")
        buildFile.createFile()
        val settingsFile = projectDir.resolve("settings.gradle.kts")

        return SharedRunnerDetails(
            projectDir.toFile(),
            buildFile.toFile(),
            settingsFile.toFile(),
            SharedRunnerDetails.SharedRunner.getRunner(testDirectory)
                .withProjectDir(projectDir.toFile())
                .withPluginClasspath()
        )
    }

}

data class TestDirectory(
    val name: String,
    val mainDirectory: Path,
    val testKitDir: Path
)

