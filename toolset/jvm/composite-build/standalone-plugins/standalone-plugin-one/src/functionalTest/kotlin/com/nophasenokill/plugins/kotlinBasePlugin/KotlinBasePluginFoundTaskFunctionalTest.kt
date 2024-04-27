package com.nophasenokill.plugins.kotlinBasePlugin

import com.nophasenokill.setup.junit.JunitTempDirFactory
import com.nophasenokill.setup.runner.SharedRunnerDetails
import com.nophasenokill.setup.variations.BlockingType
import com.nophasenokill.setup.variations.FunctionalTest
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.io.CleanupMode
import org.junit.jupiter.api.io.TempDir
import java.io.File

class KotlinBasePluginFoundTaskFunctionalTest: FunctionalTest() {


    @Test
    fun `can run task`()  = runTest  {
        val details = getAsyncResult(BlockingType) {
            val runner = SharedRunnerDetails.SharedRunner.getRunner(sharedRunnerDirLazy.value)
            createGradleRunner(runner)
        }

        val settingsFile = details.settingsFile
        val buildFile = details.buildFile
        val runner = details.gradleRunner

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
            runner.withArguments("greeting", "--warning-mode=all").build()
        }

        assertTrue(result.output.contains("Hello from plugin 'com.nophasenokill.kotlin-base-plugin'"))
    }
}