package mypackage

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class AppTest {

    @Test
    fun `app should run`() {

        val output = captureOutput {
            App.main(arrayOf())
        }

        val expectedOutput = """
            Using SomeKotlinFile.doSomething() inside of java file. Value: Bob smith
            Returning java value of: /b from doWork()
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
