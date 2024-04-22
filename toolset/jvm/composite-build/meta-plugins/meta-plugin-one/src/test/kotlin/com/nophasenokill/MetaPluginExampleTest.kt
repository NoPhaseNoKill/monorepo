package com.nophasenokill

import kotlinx.coroutines.test.runTest
import org.gradle.api.logging.Logging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MetaPluginExampleTest {

    @Test
    fun something() {
       val result = runTest {
                val bob = "bob"

                Logging.getLogger("SharedAppExtension").lifecycle("Should be debuggable: $bob")

                Assertions.assertEquals(2 + 2, 4)
        }

        Logging.getLogger("SharedAppExtension").lifecycle("Result is: ${result.toString()}")
    }
    // fun doSomething() = runTest {

    // }
}