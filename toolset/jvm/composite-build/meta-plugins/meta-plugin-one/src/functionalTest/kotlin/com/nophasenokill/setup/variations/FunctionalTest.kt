

package com.nophasenokill.setup.variations

import com.nophasenokill.setup.junit.extensions.GradleRunnerExtension
import com.nophasenokill.setup.runner.SharedRunnerDetails
import org.gradle.api.logging.Logging
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.extension.ExtendWith
import java.io.File
import java.nio.file.Path

@ExtendWith(GradleRunnerExtension::class)
open class FunctionalTest {

    val INDENT: String
        get() = "                "

    fun runExpectedSuccessTask(details: SharedRunnerDetails, task: String): BuildResult {
        return details.gradleRunner.withArguments(task, "--warning-mode=all").build()
    }

    fun getTaskOutcome(taskPath: String, result: BuildResult): TaskOutcome {
        try {
            return requireNotNull(result.task(taskPath)?.outcome)
        } catch (e: Exception) {
            Logging.getLogger("SharedAppExtension").debug("Task outcome could not be found for task path '${taskPath}'. Exception was ${e.message}")
            throw e
        }
    }

    fun addPluginsById(plugins: List<String>, buildFileToAddPluginsTo: File) {

        val formattedPlugins = plugins.joinToString(prefix = INDENT, separator = "\n$INDENT") {
            "id(\"$it\")"
        }

        val text = """
            plugins {
$formattedPlugins
            }
        """.trimIndent()

        buildFileToAddPluginsTo.writeText(text)
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

    fun getComparableBuildResultLines(result: BuildResult, trimmedFromTop: Int, trimmedFromBottom: Int): List<String> {
        val removeStartOfFile: List<String> =  result.output.lines().subList(trimmedFromTop, result.output.lines().size)
        return removeStartOfFile.subList(0, removeStartOfFile.size - trimmedFromBottom)
    }

    fun createFile(file: File) {
        file.createNewFile()
    }

    fun writeText(file: File, textBlock:() -> String) {
        /*
            Putting this outside of the IO context ensures that if the text block
            is computationally expensive we avoid putting this on the IO thread
         */
        val text = textBlock()
        file.writeText(text)
    }

    fun appendText(file: File, textBlock:() -> String) {
        /*
            Putting this outside of the IO context ensures that if the text block
            is computationally expensive we avoid putting this on the IO thread
         */
        val text = textBlock()
        file.appendText(text)
    }

    fun readLines(buildResult: BuildResult): List<String> {
        return buildResult.output.lines()
    }
}

data class TestDirectory(
    val name: String,
    val mainDirectory: Path,
)

