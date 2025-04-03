package com.nophasenokill

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class ApplicationTwoTest {

    @Test
    fun `app should run`() {

        val output = captureTestOutput {
            ApplicationTwoApp.main()
        }

        val expectedOutput = """
            Hello world!
            """.trimIndent()
        assertEquals(expectedOutput, output.trim())
    }

    @Test
    fun `app should run2`() {

        val output = captureTestOutput {
            ApplicationTwoApp.main()
        }

        val expectedOutput = """
            Hello world!
            """.trimIndent()
        assertEquals(expectedOutput, output.trim())
    }
}
