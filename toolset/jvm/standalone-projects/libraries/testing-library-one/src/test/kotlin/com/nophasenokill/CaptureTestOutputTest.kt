package com.nophasenokill

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CaptureTestOutputTest {

    @Test
    fun `should capture test output`() {

        val output = captureTestOutput {
            println("Hello, Kotlin!")
        }
        assertEquals("Hello, Kotlin!", output)
    }

    @Test
    fun `should capture trimmed test output`() {

        val whitespace = "  "
        val output = captureTestOutput {
            println("Hello, Kotlin!${whitespace}")
        }
        assertEquals("Hello, Kotlin!", output)
    }
}
