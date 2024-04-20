package com.nophasenokill.plugins.checkKotlinBuildServiceFixPlugin.someNewDir2

import com.nophasenokill.setup.variations.SharedAppContextKey
import com.nophasenokill.setup.variations.SharedAppStore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource
import java.io.FileWriter
import java.io.Writer

class SomeExtension: AfterAllCallback, CloseableResource {

    lateinit var writer: Writer

    override fun afterAll(context: ExtensionContext) = runTest {
        writer = FileWriter.nullWriter()
        println("After all for ${context.displayName}")
        Thread.sleep(1000)
        SharedAppStore.putObjectIntoGlobalStore(context, SharedAppContextKey.TESTS_STARTED, this@SomeExtension)
        return@runTest
    }

    override fun close() {

        /*

        // TODO WHERE IS MISSING 253???

            Gradle Test Executor 254 STANDARD_OUT
            HELLOOOOOOOOOOOOOO
            Closing

            SomeTest2 PASSED

            Gradle Test Executor 252 STANDARD_OUT
                HELLOOOOOOOOOOOOOO
                Closing

            SomeTest1 PASSED

            Gradle Test Executor 251 STANDARD_OUT
                HELLOOOOOOOOOOOOOO
                Closing

         */

        writer.close()
        println("HELLOOOOOOOOOOOOOO")
        println("Closing")
    }
}