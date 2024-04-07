package com.nophasenokill.functionalTest

import org.junit.jupiter.api.extension.AnnotatedElementContext
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.io.TempDirFactory
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import kotlin.system.measureTimeMillis

object JunitTempDirFactory : TempDirFactory {
    @Throws(IOException::class)
    override fun createTempDirectory(
        elementContext: AnnotatedElementContext,
        extensionContext: ExtensionContext
    ): Path {
        var tempDirFactory: Path? = null

        val time = measureTimeMillis {
            tempDirFactory = Files.createTempDirectory("${extensionContext.displayName}-")
        }

        TestLogger.LOGGER.info { "Time to create temp dir was: ${time}"}

        return requireNotNull(tempDirFactory)

    }
}