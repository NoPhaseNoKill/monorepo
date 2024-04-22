package com.nophasenokill.plugins.checkKotlinBuildServiceFixPlugin.someNewDir

import com.nophasenokill.setup.variations.IntegrationTest
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext

class SomeFileTest3: IntegrationTest() {

    @Test
    fun someFileTest3DoSomething(context: ExtensionContext) = runTest {
        Thread.sleep(2000)
        Assertions.assertEquals(2 + 2, 4)
        val root = getStoreRoot(context)
        val namespace = getGlobalNamespace()
        LOGGER.lifecycle("Root is: {}, for {}", root, context.displayName)
        LOGGER.lifecycle("GLOBAL_NAMESPACE is: {}, for {}", namespace, context.displayName)

        Assertions.assertEquals(2 + 2, 4)
        LOGGER.lifecycle("This should always occur after test 1 and 2", namespace, context.displayName)
    }
}