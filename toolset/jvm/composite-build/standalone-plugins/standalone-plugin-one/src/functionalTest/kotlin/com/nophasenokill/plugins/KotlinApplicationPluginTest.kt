package com.nophasenokill.plugins

import com.nophasenokill.functionalTest.FunctionalTest
import com.nophasenokill.functionalTest.GradleTestRunner
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class KotlinApplicationPluginTest: FunctionalTest() {

    @Test
    fun `should have only the configured dependencies`() {
        settingsFile.writeText("")
        addPluginsById(
            listOf(
                "com.nophasenokill.kotlin-application-plugin"
            ),
            buildFile
        )

        val result = GradleTestRunner.runTask("dependencies", projectDir)

        val file = getResourceFile("dependencies/kotlin-application-expected-dependencies.txt")
        val expectedContent = file.readText()

        /*
            The contents of this file don't include shifting behaviour.

            Meaning: These lines should never change unless a configuration is updated or modified in a way
            that affects dependencies
         */
        Assertions.assertTrue(result.output.contains(expectedContent))
    }

}