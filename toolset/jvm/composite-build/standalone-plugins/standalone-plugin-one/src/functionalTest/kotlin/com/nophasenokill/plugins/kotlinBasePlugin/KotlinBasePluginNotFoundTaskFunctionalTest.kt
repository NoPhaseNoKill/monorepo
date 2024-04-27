package com.nophasenokill.plugins.kotlinBasePlugin

import com.nophasenokill.setup.runner.SharedRunnerDetails
import com.nophasenokill.setup.variations.BlockingType
import com.nophasenokill.setup.variations.FunctionalTest
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class KotlinBasePluginNotFoundTaskFunctionalTest: FunctionalTest() {

    @Test
    fun `cannot run task that does not exist`()  = runTest  {

        val details = getAsyncResult(BlockingType) {
            val runner = SharedRunnerDetails.SharedRunner.getRunner(sharedRunnerDirLazy.value)
            createGradleRunner(runner)
        }

        val settingsFile = details.settingsFile
        val buildFile = details.buildFile
        val projectDir = details.projectDir

        launchAsyncWork(BlockingType) {
            settingsFile.writeText("")
            val plugins =  listOf(
                "com.nophasenokill.kotlin-base-plugin"
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


        val result = getAsyncResult(BlockingType) {
            runExpectedFailureTask(details, "someNonExistentTask")
        }

        assertEquals(null, result.task(":someNonExistentTask")?.outcome)
        assertTrue(result.output.contains("Task 'someNonExistentTask' not found in root project '${projectDir.name}'"))
    }
}