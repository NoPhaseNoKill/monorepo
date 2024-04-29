package com.nophasenokill.plugins.kotlinBasePlugin

import com.nophasenokill.setup.runner.SharedRunnerDetails
import com.nophasenokill.setup.variations.FunctionalTest.INDENT
import com.nophasenokill.setup.variations.FunctionalTest.createGradleRunner
import com.nophasenokill.setup.variations.FunctionalTest.getAsyncResult
import com.nophasenokill.setup.variations.FunctionalTest.launchAsyncWork
import com.nophasenokill.setup.variations.sharedRunnerDirLazy
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class KotlinBasePluginFoundTaskFunctionalTest {


    @Test
    fun `can run task`()  = runTest  {
        val details = getAsyncResult {
            val runner = SharedRunnerDetails.SharedRunner.getRunner(sharedRunnerDirLazy().value)
            createGradleRunner(runner)
        }

        val settingsFile = details.settingsFile
        val buildFile = details.buildFile
        val runner = details.gradleRunner

        launchAsyncWork {
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

        val result = getAsyncResult {
            runner.withArguments("greeting", "--warning-mode=all").build()
        }

        assertTrue(result.output.contains("Hello from plugin 'com.nophasenokill.kotlin-base-plugin'"))
    }
}