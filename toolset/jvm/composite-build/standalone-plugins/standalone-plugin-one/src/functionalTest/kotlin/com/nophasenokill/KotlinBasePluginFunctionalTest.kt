package com.nophasenokill

import com.nophasenokill.functionalTest.FunctionalTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class KotlinBasePluginFunctionalTest: FunctionalTest() {

    @Test
    fun `can run task`() {

        settingsFile.writeText("")

        addPluginsById(
            listOf(
                "com.nophasenokill.kotlin-base-plugin"
            ),
            buildFile
        )

        val result = runExpectedSuccessTask("greeting")
        assertTrue(result.output.contains("Hello from plugin 'com.nophasenokill.kotlin-base-plugin'"))
    }

    @Test
    fun `cannot run task that does not exist`() {
        settingsFile.writeText("")
        addPluginsById(
            listOf(
                "com.nophasenokill.kotlin-base-plugin"
            ),
            buildFile
        )

        val result = runExpectedFailureTask("someNonExistentTask")

        assertEquals(null, result.task(":someNonExistentTask")?.outcome)
        assertTrue(result.output.contains("Task 'someNonExistentTask' not found in root project '${projectDir.name}'"))
    }
}