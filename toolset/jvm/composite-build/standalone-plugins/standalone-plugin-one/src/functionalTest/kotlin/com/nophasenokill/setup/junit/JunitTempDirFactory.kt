package com.nophasenokill.setup.junit


import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.extension.AnnotatedElementContext
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.io.TempDirFactory
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

object JunitTempDirFactory : TempDirFactory {

    @Throws(IOException::class)
    override fun createTempDirectory(
        elementContext: AnnotatedElementContext,
        extensionContext: ExtensionContext
    ): Path {

        val file = lazy {
            Files.createTempDirectory("${extensionContext.displayName}-")
        }

        return file.value
    }

    fun createTempDirectory2(
        extensionContext: ExtensionContext
    ): Path {

        val file = lazy {
            Files.createTempDirectory("${extensionContext.displayName}-")
        }

        return file.value
    }
}