package com.nophasenokill.plugins.kotlinBasePlugin

import com.nophasenokill.setup.variations.FunctionalTest
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext

class KotlinBasePluginFoundTaskFunctionalTest: FunctionalTest() {

    @Test
    fun `can run task`()  {
        val details = createGradleRunner()
        val settingsFile = details.settingsFile
        val buildFile = details.buildFile

        settingsFile.writeText("")

        addPluginsById(
            listOf(
                "com.nophasenokill.kotlin-base-plugin"
            ),
            buildFile
        )

        val result = runExpectedSuccessTask(details, "greeting")
        assertTrue(result.output.contains("Hello from plugin 'com.nophasenokill.kotlin-base-plugin'"))
    }
}