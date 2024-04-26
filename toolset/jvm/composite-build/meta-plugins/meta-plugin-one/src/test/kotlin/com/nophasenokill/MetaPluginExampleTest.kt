package com.nophasenokill

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MetaPluginExampleTest {

    @Test
    fun something() {
       runTest {
                Assertions.assertEquals(2 + 2, 4)
        }
    }
}