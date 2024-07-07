package com.nophasenokill

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class LibraryTwoCalculatorTest {

    @Test
    fun `should multiply one number with another`() {
        val result = LibraryTwoCalculator.multiply(2, 2)
        val expected = 4

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun `should showcase using a dependency we didnt declare in the build file explicitly`() {
        var someNumber: Int? = null

        fun setNumber(value: Int) {
            println("Setting number to: $value")
            someNumber = value
        }

        runBlocking {
            launch {
                delay(1000)
                setNumber(20)
            }

            launch {
                delay(500)
                setNumber(15)
            }

            setNumber(10)
        }


        Assertions.assertEquals(someNumber, 20)
    }
}