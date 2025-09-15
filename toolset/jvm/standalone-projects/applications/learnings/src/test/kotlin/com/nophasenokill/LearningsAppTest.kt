package com.nophasenokill

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LearningsAppTest {

    @Test
    fun `should print individual node values of linked list`() {

        val output = captureTestOutput {
            val head = setupLinkedList()
            LearningsApp.printLinkedList(head)
        }

        val expectedOutput = """
        A
        B
        C
        D
        """.trimIndent()

        assertEquals(expectedOutput, output.trim())
    }

    @Test
    fun `should print individual node values of linked list when using recursion`() {

        val output = captureTestOutput {
            val head = setupLinkedList()
            LearningsApp.printLinkedListRecursive(head)
        }

        val expectedOutput = """
        A
        B
        C
        D
        """.trimIndent()

        assertEquals(expectedOutput, output.trim())
    }

    // Represents the equivalent of: A -> B -> C -> D
    private fun setupLinkedList(): Node {
        val a = Node("A")
        val b = Node("B")
        val c = Node("C")
        val d = Node("D")

        a.next = b
        b.next = c
        c.next = d
        // d's .next is set to null it in the constructor

        return a
    }
}
