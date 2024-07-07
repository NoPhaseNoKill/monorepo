package com.nophasenokill

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream


class ApplicationOneTest {

    @Test
    fun `app should run`() {

        val output = captureOutput {
            ApplicationOneApp.main()
        }

        val expectedOutput = """
            Hello world!
            Using the library calculator. LibraryOneCalculator.add(2, 2): 4
            """.trimIndent()
        assertEquals(expectedOutput, output.trim())
    }

    private fun captureOutput(block: () -> Unit): String {
         ByteArrayOutputStream().use { stream ->
             PrintStream(stream) .use { printStream ->
                 System.setOut(printStream)
                 try {
                     block()
                 } finally {
                     System.setOut(System.out)
                 }
                 return stream.toString()
             }
        }
    }
}