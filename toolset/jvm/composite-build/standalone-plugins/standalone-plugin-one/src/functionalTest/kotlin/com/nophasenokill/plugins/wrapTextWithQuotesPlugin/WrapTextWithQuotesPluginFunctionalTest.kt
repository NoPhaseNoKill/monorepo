package com.nophasenokill.plugins.wrapTextWithQuotesPlugin

import com.nophasenokill.setup.runner.SharedRunnerDetails
import com.nophasenokill.setup.variations.*
import com.nophasenokill.setup.variations.FunctionalTest.INDENT
import com.nophasenokill.setup.variations.FunctionalTest.createGradleRunner
import com.nophasenokill.setup.variations.FunctionalTest.getAsyncResult
import com.nophasenokill.setup.variations.FunctionalTest.getResourceFile
import com.nophasenokill.setup.variations.FunctionalTest.getTaskOutcome
import com.nophasenokill.setup.variations.FunctionalTest.launchAsyncWork
import com.nophasenokill.setup.variations.FunctionalTest.runExpectedSuccessTask
import kotlinx.coroutines.test.runTest
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class WrapTextWithQuotesPluginFunctionalTest {

    @Test
    fun `finds and runs addQuotationMarks task successfully`() = runTest {

        val details = getAsyncResult {
            val runner = SharedRunnerDetails.SharedRunner.getRunner(sharedRunnerDirLazy().value)
            createGradleRunner(runner)
        }


        val projectDir = lazyProvider(details.projectDir)
        val settingsFile = lazyOf(details.settingsFile)
        val buildFile = lazyOf(details.buildFile)

        val plugins = lazyOf(
            listOf(
                "com.nophasenokill.wrap-text-with-quotes-plugin"
            )
        )

        val formattedPlugins = lazyOf(plugins.value.joinToString(prefix = INDENT, separator = "\n$INDENT") {
            "id(\"$it\")"
        })

        val buildFileText = lazyOf(
            """
            plugins {
$formattedPlugins
            }
        """.trimIndent()
        )

        val input = projectDir.get().resolve("example-text.txt")
        val output = projectDir.get().resolve("example-text-output-pre-quote-adding.txt")

        val baseFileText = getAsyncResult {
            getResourceFile("wrap-text-with-quotes-plugin-files/example-text.txt").readText()
        }



        launchAsyncWork {
            // Should have no quotes to start
            Assertions.assertEquals("Bond. James Bond.", baseFileText)

            settingsFile.value.writeText("")
            buildFile.value.writeText(buildFileText.value)

            input.writeText(baseFileText)
            output.writeText(baseFileText)
        }

        val result = getAsyncResult {
            runExpectedSuccessTask(
                details.gradleRunner,
                "addQuotationMarks"
            )
        }

        val outcome = getAsyncResult {
            getTaskOutcome(":addQuotationMarks", result)
        }

        Assertions.assertEquals(outcome, TaskOutcome.SUCCESS)
        Assertions.assertEquals("\"Bond. James Bond.\"", output.readText())

    }
}