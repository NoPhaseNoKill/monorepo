package com.integraboost

import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.KotlinClosure2
import org.gradle.kotlin.dsl.withType

fun Project.configureTestLogging() {

    tasks.withType<Test> {
        systemProperties["java.util.logging.config.file"] = "${project.buildDir}/resources/test/logging.properties"

        useJUnitPlatform()

        testLogging {
            // set options for log level LIFECYCLE
            events = setOf(
                TestLogEvent.FAILED,
                TestLogEvent.PASSED,
                TestLogEvent.SKIPPED,
            )
            exceptionFormat = TestExceptionFormat.FULL
            showExceptions = true
            showCauses = true
            showStackTraces = true

            // set options for log level DEBUG and INFO
            debug {
                events = setOf(
                    TestLogEvent.STARTED,
                    TestLogEvent.FAILED,
                    TestLogEvent.PASSED,
                    TestLogEvent.SKIPPED,
                    TestLogEvent.STANDARD_ERROR,
                )
                exceptionFormat = TestExceptionFormat.FULL
            }
            info.events = debug.events
            info.exceptionFormat = debug.exceptionFormat

            afterSuite(KotlinClosure2({ desc: TestDescriptor, result: TestResult ->
                if (desc.parent == null) { // will match the outermost suite
                    val pass = "${Color.GREEN}Passed: ${result.successfulTestCount} ${Color.NONE}"
                    val fail = "${Color.RED}Failed: ${result.failedTestCount} ${Color.NONE}"
                    val skip = "${Color.YELLOW}Skipped: ${result.skippedTestCount}${Color.NONE}"
                    val typeColour = when(result.resultType) {
                        TestResult.ResultType.SUCCESS -> Color.GREEN
                        TestResult.ResultType.FAILURE -> Color.RED
                        TestResult.ResultType.SKIPPED -> Color.YELLOW
                        else -> throw Exception("Unknown result type colour for test outputs")
                    }
                    val resultType = result.resultType

                    val type = "${typeColour}$resultType${Color.NONE}"
                    val total = result.successfulTestCount + result.failedTestCount + result.skippedTestCount

                    println("")
                    println("""
                        ${Color.CYAN}Test result summary: ${type}${Color.NONE}
                            $skip
                            $fail
                            $pass
                            ${Color.PURPLE}Total:  $total${Color.NONE}
                    """.trimIndent())
                }
            }))
        }
    }
}

operator fun String.times(x: Int): String {
    return List(x) { this }.joinToString("")
}

internal enum class Color(ansiCode: Int) {
    NONE(0),
    BLACK(30),
    RED(31),
    GREEN(32),
    YELLOW(33),
    BLUE(34),
    PURPLE(35),
    CYAN(36),
    WHITE(37);

    private val ansiString: String = "\u001B[${ansiCode}m"

    override fun toString(): String {
        return ansiString
    }
}
