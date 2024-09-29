package com.nophasenokill

import java.io.File

object MethodTracker {
    private val logFile = File("test-method-mapping.txt")

    private var currentTest: String? = null

    fun logMethod(method: String) {
        // Log methods only if a test is running
        if (currentTest != null) {
            logFile.appendText("  Method: $method\n")
        }
    }

    fun startTest(testClassMethod: String) {
        currentTest = testClassMethod
        logFile.appendText("Test: $testClassMethod\n")
    }

    fun endTest() {
        currentTest?.let {
            logFile.appendText("End of Test: $it\n\n")
            currentTest = null
        }
    }

    fun resetLog() {
        logFile.writeText("") // Clear the log file
    }
}
