package com.nophasenokill

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test


class TestInstrumentationAppKtApplicationWith {

    @Test
    @Disabled
    fun checkDoSomethingIsBeingInstrumented() {
        val result = TestInstrumentationApp.doSomething()
        val expected = "App HAS been instrumented"

        Assertions.assertEquals(expected, result)
    }
}