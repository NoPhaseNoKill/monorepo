package com.nophasenokill.plugins.checkKotlinBuildServiceFixPlugin.someNewDir

import com.nophasenokill.setup.junit.extensions.GradleRunnerExtension
import com.nophasenokill.setup.variations.IntegrationTest
import com.nophasenokill.setup.variations.SharedAppExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(GradleRunnerExtension::class)
class SomeFileTest2: IntegrationTest() {

    @Test
    fun someFileTest2DoSomething(context: ExtensionContext) = runTest {
        Assertions.assertEquals(2 + 2, 4)
        val root = SharedAppExtension.storeRoot(context)
        println("Root is: $root for ${context.displayName}")
        println("GLOBAL_NAMESPACE is: ${SharedAppExtension.GLOBAL_NAMESPACE} for ${context.displayName}")
    }
}