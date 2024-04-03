package com.nophasenokill

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ExampleTest {

    @Test
    fun shouldBeDebuggable() {
        val someVariable = "I should be debuggable"

        println("This should be debuggable: $someVariable")

        Assertions.assertEquals(2 + 2, 4)
    }
}