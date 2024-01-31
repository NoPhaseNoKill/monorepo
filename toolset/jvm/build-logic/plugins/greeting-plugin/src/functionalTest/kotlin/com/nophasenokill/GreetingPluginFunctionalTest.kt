package com.nophasenokill

import java.io.File
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class GreetingPluginFunctionalTest {

    @field:TempDir
    lateinit var projectDir: File

    private val buildFile by lazy { projectDir.resolve("build.gradle") }
    private val settingsFile by lazy { projectDir.resolve("settings.gradle") }

    @Test
    fun `can run task`() {
        // Set up the test build
        settingsFile.writeText("")
        buildFile.writeText("""
            plugins {
                id('greeting-plugin')
            }
        """.trimIndent())

        // Run the build
        val runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments("taskInsideGreetingPlugin")
        runner.withProjectDir(projectDir)
        val result = runner.build()

        // Verify the result
        assertTrue(result.output.contains("Hello from plugin 'greeting-plugin'"))
    }
}
