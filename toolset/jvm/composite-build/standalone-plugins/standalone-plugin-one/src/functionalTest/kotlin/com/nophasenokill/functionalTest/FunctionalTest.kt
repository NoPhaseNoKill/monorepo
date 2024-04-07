package com.nophasenokill.functionalTest

import com.nophasenokill.extensions.SharedTestSuiteExtension
import com.nophasenokill.extensions.TestInvocationListener
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.io.CleanupMode
import org.junit.jupiter.api.io.TempDir
import java.io.File


@ExtendWith(SharedTestSuiteExtension::class, TestInvocationListener::class)
open class FunctionalTest: GradleTestCapabilities {

    @field:TempDir(factory = JunitTempDirFactory::class, cleanup = CleanupMode.ON_SUCCESS)
    override lateinit var projectDir: File

    /*
        Consider adding these in the future if we need them:
            .forwardOutput()
            .forwardStdError()
            .forwardStdOutput()
     */
    private val runner: GradleRunner by lazy { GradleRunner.create()
        .withProjectDir(projectDir)
        .withPluginClasspath() }

    override val buildFile by lazy { projectDir.resolve("build.gradle.kts") }

    override val settingsFile by lazy { projectDir.resolve("settings.gradle.kts") }

    private val INDENT = "                "

    override fun addPluginsById(plugins: List<String>, buildFileToAddPluginsTo: File) {

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

    override fun runExpectedSuccessTask(task: String): BuildResult {
        return runner.withArguments(task, "--stacktrace").build()
    }

    override fun runExpectedFailureTask(task: String): BuildResult {
        return runner.withArguments(task, "--stacktrace").buildAndFail()
    }

    override fun getTaskOutcome(taskPath: String, result: BuildResult): TaskOutcome {
        try {
            return requireNotNull(result.task(taskPath)?.outcome)
        } catch (e: Exception) {
            TestLogger.LOGGER.error {"Task outcome could not be found for task path '${taskPath}'. Exception was ${e.message}" }
            throw e
        }
    }

    override fun getComparableBuildResultLines(result: BuildResult, trimmedFromTop: Int, trimmedFromBottom: Int): List<String> {
        val removeStartOfFile: List<String> =  result.output.lines().subList(trimmedFromTop, result.output.lines().size)
        return removeStartOfFile.subList(0, removeStartOfFile.size - trimmedFromBottom)
    }

    /*
        This ensures the test is relocatable for cache, as the file should always be relative
     */
    override fun getResourceFile(fileNamePath: String): File {
        val classLoader = Thread.currentThread().contextClassLoader
        val resourceURL = requireNotNull(
            classLoader.getResource(fileNamePath)
        )
        return File(resourceURL.toURI())
    }
}