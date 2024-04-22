package com.nophasenokill.plugins.checkKotlinBuildServiceFixPlugin.someNewDir

import com.nophasenokill.setup.variations.IntegrationTest
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext

class SomeFileTest: IntegrationTest() {

    @Test
    fun someFileTest1DoSomething(context: ExtensionContext) = runTest {
        Assertions.assertEquals(2 + 2, 4)
        val root = getStoreRoot(context)
        val namespace = getGlobalNamespace()
        LOGGER.lifecycle("Root is: {}, for {}", root, context.displayName)
        LOGGER.lifecycle("GLOBAL_NAMESPACE is: {}, for {}", namespace, context.displayName)

        Assertions.assertEquals(2 + 2, 4)
        LOGGER.lifecycle("This should always occur before test 2 and 3", namespace, context.displayName)
    }
}