package com.nophasenokill.plugins.checkKotlinBuildServiceFixPlugin.someNewDir

import com.nophasenokill.setup.variations.IntegrationTest
import com.nophasenokill.setup.variations.SharedAppExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext

class SomeFileTest2(private val context: ExtensionContext): IntegrationTest(context) {

    @Test
    fun someFileTest2DoSomething() {
        Assertions.assertEquals(2 + 2, 4)
        val root = SharedAppExtension.storeRoot(context)
        println("Root is: $root for ${context.displayName}")
        println("GLOBAL_NAMESPACE is: ${SharedAppExtension.GLOBAL_NAMESPACE} for ${context.displayName}")
    }
}