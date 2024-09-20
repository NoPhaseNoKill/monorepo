package com.nophasenokill

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ClassToBeInstrumentedAtRuntimeTest {

    @Test
    fun shouldShowcaseInstrumentation() {
        val annotatedClass = ClassToBeInstrumentedAtRuntime()
        val expected = "Instrumented"
        assertEquals(expected, annotatedClass.toString())
    }
}
