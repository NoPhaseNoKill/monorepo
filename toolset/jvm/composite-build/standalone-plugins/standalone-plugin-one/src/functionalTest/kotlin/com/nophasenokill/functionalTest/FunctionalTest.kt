package com.nophasenokill.functionalTest

import com.nophasenokill.extensions.SharedTestSuiteExtension
import com.nophasenokill.extensions.TestInvocationListener
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.io.CleanupMode
import org.junit.jupiter.api.io.TempDir
import java.io.File


@ExtendWith(SharedTestSuiteExtension::class, TestInvocationListener::class)
open class FunctionalTest {

    @field:TempDir(factory = JunitTempDirFactory::class, cleanup = CleanupMode.ON_SUCCESS)
    lateinit var projectDir: File

    val buildFile by lazy { projectDir.resolve("build.gradle.kts") }

    val settingsFile by lazy { projectDir.resolve("settings.gradle.kts") }

    fun addPluginsById(plugins: List<String>, buildFileToAddPluginsTo: File) {

        val formattedPlugins = plugins.joinToString(prefix = "id(\"", postfix = "\")", separator = ",\n") {
            it
        }

        val text = """
            plugins {
                $formattedPlugins
            }
        """.trimIndent()

        return buildFileToAddPluginsTo.writeText(text)
    }

    /*
        This ensures the test is relocatable for cache, as the file should always be relative
     */
    fun getResourceFile(fileNamePath: String): File {
        val classLoader = Thread.currentThread().contextClassLoader
        val resourceURL = requireNotNull(
            classLoader.getResource(fileNamePath)
        )
        return File(resourceURL.toURI())
    }
}