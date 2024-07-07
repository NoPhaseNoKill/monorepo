package com.nophasenokill

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class LibraryTwoCalculatorTest {

    @Test
    fun `should multiply one number with another`() {
        val result = LibraryTwoCalculator.multiply(2, 2)
        val expected = 4

        Assertions.assertEquals(expected, result)
    }
}