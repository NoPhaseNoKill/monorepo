package com.nophasenokill.setup.junit

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.extension.AnnotatedElementContext
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.io.TempDirFactory
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

object JunitTempDirFactory : TempDirFactory {
    @Throws(IOException::class)
    override fun createTempDirectory(
        elementContext: AnnotatedElementContext,
        extensionContext: ExtensionContext
    ): Path  = runBlocking {
        return@runBlocking Files.createTempDirectory("${extensionContext.displayName}-")
    }
}