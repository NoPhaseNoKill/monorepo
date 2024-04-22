package com.nophasenokill.plugins.checkKotlinBuildServiceFixPlugin.someNewDir

import com.nophasenokill.setup.variations.IntegrationTest
import com.nophasenokill.setup.variations.SharedAppExtension
import kotlinx.coroutines.test.runTest
import org.gradle.api.logging.Logging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext

class SomeFileTest3: IntegrationTest() {

    @Test
    fun someFileTest3DoSomething(context: ExtensionContext) = runTest {
        Assertions.assertEquals(2 + 2, 4)
        val root = getStoreRoot(context)
        val namespace = getGlobalNamespace()
        Logging.getLogger("SharedAppExtension").lifecycle("Root is: ${root}, for ${ context.displayName}")
        Logging.getLogger("SharedAppExtension").lifecycle("GLOBAL_NAMESPACE is: {}, for {}", namespace, context.displayName)}
}