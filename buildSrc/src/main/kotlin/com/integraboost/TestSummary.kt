package com.integraboost

import org.gradle.api.tasks.testing.TestResult

interface TestLineType {
    val identifier: String
    val identifierColour: AnsiColour
}

enum class TestResultType(
    override val identifier: String,
    override val identifierColour: AnsiColour,
) : TestLineType {
    PASSED("Passed", AnsiColour.GREEN),
    SKIPPED("Skipped", AnsiColour.YELLOW),
    FAILED("Failed", AnsiColour.RED),
    TOTAL("Total", AnsiColour.PURPLE);
}

enum class TestOutcomeType(
    override val identifier: String,
    override val identifierColour: AnsiColour,
) : TestLineType {
    SUCCESSFUL("Test suite successful", AnsiColour.CYAN),
    FAILED("Test suite failed", AnsiColour.CYAN);
}


interface TestOutputLine : TestLineType {
    val value: String
    val valueColour: AnsiColour

    fun line(prefix: String = "", padding: Int = 0): String {
        val paddedId = "$prefix$identifier".padEnd(padding)
        val colouredId = colourize(paddedId, identifierColour)
        val colouredValue = colourize(value, valueColour)
        return "$colouredId : $colouredValue"
    }

    private fun colourize(input: String, colour: AnsiColour) = Colourizer.colourize(input, colour)
}

data class TestResult(
    private val type: TestResultType,
    private val result: TestResult
) : TestOutputLine {
    override val identifier = type.identifier
    override val identifierColour = type.identifierColour
    override val valueColour = type.identifierColour
    override val value = when (type) {
        TestResultType.PASSED -> result.successfulTestCount.toString()
        TestResultType.SKIPPED -> result.skippedTestCount.toString()
        TestResultType.FAILED -> result.failedTestCount.toString()
        TestResultType.TOTAL -> result.testCount.toString()
    }
}

data class TestOutcome(
    private val result: TestResult,
) : TestOutputLine {
    private val type = if(result.failedTestCount > 0) TestOutcomeType.FAILED else TestOutcomeType.SUCCESSFUL

    override val identifier = type.identifier
    override val identifierColour = type.identifierColour
    override val valueColour = when (type) {
        TestOutcomeType.FAILED -> AnsiColour.RED
        TestOutcomeType.SUCCESSFUL -> AnsiColour.GREEN
    }
    override val value = type.name
}

class TestSummary(
    val result: TestResult,
) {

    fun summarise(): String {
        val lines = getOutputLines(result)
        val padding = lines.maxOf { it.identifier.length } + 10 // Add 10 for padding
        val outcome = lines.first() // Assume that the outcome is the first line
        val results = lines.drop(1) // All lines except the outcome
        val outcomeLine = outcome.line(padding = padding)
        val resultLines = results.joinToString(separator = System.lineSeparator()) { it.line(prefix = "|----> ", padding = padding) }
        return "$outcomeLine${System.lineSeparator()}$resultLines"
    }

    private fun getOutputLines(result: TestResult): Set<TestOutputLine> {
        val testResultTypes = enumValues<TestResultType>()
        val testResults: List<TestOutputLine> = testResultTypes.map { TestResult(it, result) }
        val outcome: TestOutputLine = TestOutcome(result)
        val outcomeResults: List<TestOutputLine> = listOf(
            outcome,
        )

        return (outcomeResults + testResults).toSet()
    }
}
