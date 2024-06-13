package com.nophasenokill

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.nio.file.Files
import kotlin.io.path.createFile
import kotlin.io.path.writeText

class PluginInteractionTest {
    @Test
    fun `test plugin dependency application from a composite build`() {

        val projectDir  = Files.createTempDirectory("shared-runner-dir")

        val buildFile = projectDir.resolve("build.gradle.kts")
        val created = buildFile.createFile()

        created.writeText("""
                plugins {
                    id("com.nophasenokill.consumer-plugin")
                }
            """)

        val result = GradleRunner.create()
            .withProjectDir(projectDir.toFile())
            .withPluginClasspath()
            .withArguments("help") // Simple task, just need to initialize the plugin
            .build()

        // Check for specific output that would only be possible if the producer plugin was applied
        assertTrue(result.output.contains("I am the producer plugin which is being applied to the ${projectDir.fileName}"))
    }
}
