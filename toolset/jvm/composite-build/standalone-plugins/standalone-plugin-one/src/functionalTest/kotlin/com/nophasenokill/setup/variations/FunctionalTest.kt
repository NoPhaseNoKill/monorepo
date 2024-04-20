package com.nophasenokill.setup.variations

import com.nophasenokill.setup.junit.JunitTempDirFactory
import com.nophasenokill.setup.logging.TestLogger
import com.nophasenokill.setup.runner.SharedRunnerDetails

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.io.CleanupMode
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Files
import java.util.*
import kotlin.io.path.createFile

// @ExtendWith(SharedTestSuiteExtension::class, TestInvocationListener::class)
// @ExtendWith(SharedTestSuiteExtension::class)

open class FunctionalTest {

    val INDENT: String
        get() = "                "

    fun runExpectedSuccessTask(details: SharedRunnerDetails, task: String): BuildResult  {
        return details.gradleRunner
            .withArguments(
                task,
                "--warning-mode=all",
            )
            .build()
    }

    fun runExpectedFailureTask(details: SharedRunnerDetails, task: String): BuildResult  {
        return details.gradleRunner.withArguments(task, "--warning-mode=all").buildAndFail()
    }

    fun getTaskOutcome(taskPath: String, result: BuildResult): TaskOutcome  {
        try {
            return requireNotNull(result.task(taskPath)?.outcome)
        } catch (e: Exception) {
            TestLogger.LOGGER.error {"Task outcome could not be found for task path '${taskPath}'. Exception was ${e.message}" }
            throw e
        }
    }

    fun getComparableBuildResultLines(result: BuildResult, trimmedFromTop: Int, trimmedFromBottom: Int): List<String> {
        val removeStartOfFile: List<String> =  result.output.lines().subList(trimmedFromTop, result.output.lines().size)
        return removeStartOfFile.subList(0, removeStartOfFile.size - trimmedFromBottom)
    }


    /*
        This ensures the test is relocatable for cache, as the file should always be relative
     */
    fun getResourceFile(fileNamePath: String): File {
        val classLoader = Thread.currentThread().contextClassLoader
        val resourceURL = requireNotNull(
            classLoader.getResource(fileNamePath)
        )
        return File(resourceURL.toURI())
    }

    fun addPluginsById(plugins: List<String>, buildFileToAddPluginsTo: File) {

        val formattedPlugins = plugins.joinToString( prefix = INDENT, separator = "\n$INDENT") {
            "id(\"$it\")"
        }

        val text = """
            plugins {
$formattedPlugins
            }
        """.trimIndent()

        buildFileToAddPluginsTo.writeText(text)
    }

    fun createGradleRunner(): SharedRunnerDetails {


        val projectDir  = Files.createTempDirectory(UUID.randomUUID().toString())

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

