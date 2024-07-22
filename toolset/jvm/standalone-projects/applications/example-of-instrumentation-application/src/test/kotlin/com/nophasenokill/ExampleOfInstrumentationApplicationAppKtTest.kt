package com.nophasenokill

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class ExampleOfInstrumentationApplicationAppKtTest {

    @Test
    fun `should showcase that when instrumenting jar file, to use for tests, calling original code actually uses instrumented code instead`() {

        val output = captureOutput {
            ExampleOfInstrumentationApplicationApp().run()
        }

        val expected = """
            Running instrumentation app

        """.trimIndent()

        assertEquals(expected, output)
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