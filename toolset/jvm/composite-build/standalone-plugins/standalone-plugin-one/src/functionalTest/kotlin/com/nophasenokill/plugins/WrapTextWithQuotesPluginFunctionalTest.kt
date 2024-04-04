package com.nophasenokill.plugins

import com.nophasenokill.functionalTest.FunctionalTest
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class WrapTextWithQuotesPluginFunctionalTest: FunctionalTest() {

    @Test
    fun `finds plugin`() {

        settingsFile.writeText("")
        addPluginsById(
            listOf(
                "com.nophasenokill.wrap-text-with-quotes-plugin",
                "java"
            ),
            buildFile
        )

        val result = runExpectedSuccessTask("build")
        val outcome = getTaskOutcome(":build", result)
        Assertions.assertTrue(result.output.contains("BUILD SUCCESS"))
        Assertions.assertEquals(outcome, TaskOutcome.SUCCESS)
    }

    @Test
    fun `finds and runs addQuotationMarks task successfully`() {

        settingsFile.writeText("")
        addPluginsById(
            listOf(
                "com.nophasenokill.wrap-text-with-quotes-plugin"
            ),
            buildFile
        )

        val quotesFile = getResourceFile("wrap-text-with-quotes-plugin-files/example-text.txt")
        val withQuotations = getResourceFile("wrap-text-with-quotes-plugin-files/example-text-output-pre-quote-adding.txt")

        // Should have no quotes to start
        Assertions.assertEquals("Bond. James Bond.", withQuotations.readText())

        val testQuotesFile = projectDir.resolve("example-text.txt")
        val testWithQuotationsFile = projectDir.resolve("example-text-output-pre-quote-adding.txt")

        testQuotesFile.writeText(quotesFile.readText())
        testWithQuotationsFile.writeText(withQuotations.readText())

        val result = runExpectedSuccessTask("addQuotationMarks")
        val outcome = getTaskOutcome(":addQuotationMarks", result)
        Assertions.assertTrue(result.output.contains("BUILD SUCCESS"))
        Assertions.assertEquals(outcome, TaskOutcome.SUCCESS)
        Assertions.assertEquals("\"Bond. James Bond.\"", testWithQuotationsFile.readText())
    }

}