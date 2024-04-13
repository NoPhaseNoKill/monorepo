package com.nophasenokill

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class StandalonePluginExampleTest {
    @Test
    fun doSomething() {
        val bob = "bob"

        println("Should be debuggable: $bob")

        Assertions.assertEquals(2 + 2, 4)
    }
}