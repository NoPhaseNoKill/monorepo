package com.nophasenokill

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.io.CleanupMode
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.system.measureTimeMillis

abstract class PluginTest {



    fun runTask(task: String): BuildResult {
        return measureTimeOfBlock("gradle runner run task") {
            GradleRunner.create()
                .withProjectDir(testProjectDir.toFile())
                .withArguments(task, "--stacktrace")
                .withPluginClasspath()
                .build()
        }
    }

    fun runTaskWithFailure(task: String): BuildResult {
        return GradleRunner.create()
            .withProjectDir(testProjectDir.toFile())
            .withArguments(task, "--stacktrace")
            .withPluginClasspath()
            .buildAndFail()
    }

    companion object {

        @field:TempDir(cleanup = CleanupMode.ON_SUCCESS)
        var testProjectDir: Path = measureTimeOfBlock("createTempDirectory") { Files.createTempDirectory("shared-runner-dir") }

        @JvmStatic
        protected lateinit var settingsFile: File

        @JvmStatic
        protected lateinit var buildFile: File

        @JvmStatic
        @BeforeAll
        fun setup(): Unit {
            val testProjectFile = testProjectDir.toFile()
            settingsFile = testProjectFile.resolve("settings.gradle.kts")
            settingsFile.appendText(
                """
                rootProject.name = "test"
            """
            )
            buildFile = testProjectFile.resolve("build.gradle.kts")
        }

        fun <T> measureTimeOfBlock(blockIdentifier: String, block: () -> T): T {
            return run {
                var result: T?
                val time = measureTimeMillis {
                    result = block()
                }
                println("Time took to run block: $blockIdentifier was: $time")
                requireNotNull(result)
            }
        }
    }
}