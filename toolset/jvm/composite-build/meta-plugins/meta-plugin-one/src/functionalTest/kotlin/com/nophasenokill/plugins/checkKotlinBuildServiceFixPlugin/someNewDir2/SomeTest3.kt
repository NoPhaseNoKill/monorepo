package com.nophasenokill.plugins.checkKotlinBuildServiceFixPlugin.someNewDir2

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(SomeExtension::class)
class SomeTest3 {

    @Test
    fun doSomething() {
        println("Test running ${this.javaClass.name}")
    }
}