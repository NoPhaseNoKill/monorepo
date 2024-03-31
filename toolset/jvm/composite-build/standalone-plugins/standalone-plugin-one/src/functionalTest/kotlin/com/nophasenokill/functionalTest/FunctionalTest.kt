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

    val buildFile by lazy { projectDir.resolve("build.gradle") }

    val settingsFile by lazy { projectDir.resolve("settings.gradle") }

    fun addPluginsById(plugins: List<String>) {

        val formattedPlugins = plugins.map {
            it
        }.joinToString (prefix = "id(\"",  postfix = "\")", separator = ",\n")

        val text = """
            plugins {
                $formattedPlugins
            }
        """.trimIndent()

        return buildFile.writeText(text)
    }
}