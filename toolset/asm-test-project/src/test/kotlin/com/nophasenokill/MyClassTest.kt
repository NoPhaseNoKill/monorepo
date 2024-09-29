package com.nophasenokill

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


class MyClassTest {
    @Test
    fun testPerformAction() {
        val myClass = MyClass()
        myClass.performAction()
        assertTrue(true)
    }
}
