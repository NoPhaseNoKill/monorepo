package com.nophasenokill.setup.variations

import com.nophasenokill.setup.junit.JunitTempDirFactory
import com.nophasenokill.setup.junit.extensions.SharedTestSuiteExtension
import com.nophasenokill.setup.junit.extensions.TestInvocationListener
import com.nophasenokill.setup.runner.SharedRunnerDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.gradle.testkit.runner.BuildResult
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.io.CleanupMode
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.api.parallel.ResourceLock
import java.io.File
import java.nio.file.Files
import kotlin.io.path.createFile

@ExtendWith(SharedTestSuiteExtension::class, TestInvocationListener::class)
@ResourceLock("gradleFunctionalTest") // Required due to gradle file locking
open class FunctionalTest {

    fun runExpectedSuccessTask(details: SharedRunnerDetails, task: String): BuildResult {
        return details.gradleRunner.forwardOutput().withArguments(task, "--warning-mode=all").build()
    }

    fun createGradleRunner(context: ExtensionContext): SharedRunnerDetails {

        val projectDir  = Files.createTempDirectory(context.displayName)

        val buildFile = projectDir.resolve("build.gradle.kts")
        buildFile.createFile()
        val settingsFile = projectDir.resolve("settings.gradle.kts")

        return SharedRunnerDetails(
            projectDir.toFile(),
            buildFile.toFile(),
            settingsFile.toFile(),
            SharedRunnerDetails.SharedRunner.getRunner(sharedRunnerDir)
                .withProjectDir(projectDir.toFile())
                .withPluginClasspath()
        )
    }

    companion object {

        @field:TempDir(factory = JunitTempDirFactory::class, cleanup = CleanupMode.ON_SUCCESS)
        lateinit var sharedRunnerDir: File
    }
}

