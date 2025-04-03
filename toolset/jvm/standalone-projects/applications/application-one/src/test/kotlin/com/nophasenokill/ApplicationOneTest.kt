package com.nophasenokill

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class ApplicationOneTest {

    @Test
    fun `app should run`() {

        val output = captureTestOutput {
            ApplicationOneApp.main()
        }

        val expectedOutput = """
            Hello world!
            Using the library calculator. LibraryOneCalculator.add(2, 2): 4
            """.trimIndent()
        assertEquals(expectedOutput, output.trim())
    }

    @Test
    fun `app should run2`() {

        val output = captureTestOutput {
            ApplicationOneApp.main()
        }

        val expectedOutput = """
            Hello world!
            Using the library calculator. LibraryOneCalculator.add(2, 2): 4
            """.trimIndent()
        assertEquals(expectedOutput, output.trim())
    }
}
