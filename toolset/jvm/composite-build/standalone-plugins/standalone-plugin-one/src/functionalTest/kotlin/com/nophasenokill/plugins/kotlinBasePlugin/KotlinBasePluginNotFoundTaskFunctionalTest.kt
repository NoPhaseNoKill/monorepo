package com.nophasenokill.plugins.kotlinBasePlugin

import com.nophasenokill.setup.variations.FunctionalTest
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext

class KotlinBasePluginNotFoundTaskFunctionalTest: FunctionalTest() {

    @Test
    fun `cannot run task that does not exist`()  {
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

        val result = runExpectedFailureTask(details, "someNonExistentTask")

        assertEquals(null, result.task(":someNonExistentTask")?.outcome)
        assertTrue(result.output.contains("Task 'someNonExistentTask' not found in root project '${details.projectDir.name}'"))
    }
}