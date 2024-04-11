package com.nophasenokill.plugins.wrapTextWithQuotesPlugin

import com.nophasenokill.setup.variations.FunctionalTest
import kotlinx.coroutines.test.runTest
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext

class WrapTextWithQuotesPluginFunctionalTest: FunctionalTest() {

    @Test
    fun `finds and runs addQuotationMarks task successfully`(context: ExtensionContext) = runTest {
        val details = createGradleRunner(context)
        val settingsFile = details.settingsFile
        val buildFile = details.buildFile
        val projectDir = details.projectDir

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

        val result = runExpectedSuccessTask(details, "addQuotationMarks")
        val outcome = getTaskOutcome(":addQuotationMarks", result)
        Assertions.assertTrue(result.output.contains("BUILD SUCCESS"))
        Assertions.assertEquals(outcome, TaskOutcome.SUCCESS)
        Assertions.assertEquals("\"Bond. James Bond.\"", testWithQuotationsFile.readText())
    }

}