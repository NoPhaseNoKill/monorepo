package com.nophasenokill

import org.junit.jupiter.api.Assertions
import kotlin.test.Test

class TestInstrumentationAppKtTest {

    @Test
    fun checkDoSomethingIsBeingInstrumented() {
        val result = TestInstrumentationApp.doSomething()
        val expected = "App HAS been instrumented"

        Assertions.assertEquals(expected, result)
    }
}