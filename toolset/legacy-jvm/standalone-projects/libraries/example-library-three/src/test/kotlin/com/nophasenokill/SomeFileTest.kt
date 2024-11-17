package com.nophasenokill

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SomeFileTest {

    @Test
    fun someTest() {
        val result = SomeFile().add(2, 2)

        Assertions.assertEquals(result, 4)
    }
}
