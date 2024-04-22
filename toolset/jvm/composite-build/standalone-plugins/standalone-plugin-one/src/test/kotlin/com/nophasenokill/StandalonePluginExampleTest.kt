package com.nophasenokill

import org.gradle.api.logging.Logging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class StandalonePluginExampleTest {
    @Test
    fun doSomething() {
        val bob = "bob"

        Logging.getLogger("SharedAppExtension").lifecycle("Should be debuggable: $bob")

        Assertions.assertEquals(2 + 2, 4)
    }
}