package com.nophasenokill

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class LibraryOneCalculatorTest {

    @Test
    fun `should add two numbers together`() {
        val result = LibraryOneCalculator.add(2, 2)
        val expected = 4

        Assertions.assertEquals(expected, result)
    }
}