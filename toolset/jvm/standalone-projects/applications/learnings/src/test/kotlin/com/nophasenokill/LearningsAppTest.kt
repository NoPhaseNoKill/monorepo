package com.nophasenokill

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LearningsAppTest {

    @Test
    fun `should print individual node values of linked list`() {

        val output = captureTestOutput {
            LearningsApp.printLinkedList()
        }

        val expectedOutput = """
        A
        B
        C
        D
        """.trimIndent()

        assertEquals(expectedOutput, output.trim())
    }
}
