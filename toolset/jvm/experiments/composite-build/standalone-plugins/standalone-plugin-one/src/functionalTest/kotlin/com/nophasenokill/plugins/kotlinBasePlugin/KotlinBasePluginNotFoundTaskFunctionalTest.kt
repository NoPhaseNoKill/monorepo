package com.nophasenokill.plugins.kotlinBasePlugin


import com.nophasenokill.setup.runner.SharedRunnerDetails
import com.nophasenokill.setup.variations.FunctionalTest.INDENT
import com.nophasenokill.setup.variations.FunctionalTest.createGradleRunner
import com.nophasenokill.setup.variations.FunctionalTest.getAsyncResult
import com.nophasenokill.setup.variations.FunctionalTest.launchAsyncWork
import com.nophasenokill.setup.variations.FunctionalTest.runExpectedFailureTask
import com.nophasenokill.setup.variations.sharedRunnerDirLazy
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class KotlinBasePluginNotFoundTaskFunctionalTest {

    @Test
    fun `cannot run task that does not exist`()  = runTest  {

        val details = getAsyncResult {
            val runner = SharedRunnerDetails.SharedRunner.getRunner(sharedRunnerDirLazy().value)
            createGradleRunner(runner)
        }

        val settingsFile = details.settingsFile
        val buildFile = details.buildFile
        val projectDir = details.projectDir

        launchAsyncWork {
            settingsFile.writeText("")
            val plugins =  listOf(
                "com.nophasenokill.standalone-plugins.kotlin-base-plugin"
            )

            val formattedPlugins = plugins.joinToString(prefix = INDENT, separator = "\n$INDENT") {
                "id(\"$it\")"
            }

            val text = """
            plugins {
$formattedPlugins
            }
        """.trimIndent()

            buildFile.writeText(text)
        }


        val result = getAsyncResult {
            runExpectedFailureTask(details, "someNonExistentTask")
        }

        assertEquals(null, result.task(":someNonExistentTask")?.outcome)
        assertTrue(result.output.contains("Task 'someNonExistentTask' not found in root project '${projectDir.name}'"))
    }
}