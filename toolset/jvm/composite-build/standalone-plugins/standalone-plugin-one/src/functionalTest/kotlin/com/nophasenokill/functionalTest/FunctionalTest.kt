package com.nophasenokill.functionalTest

import com.nophasenokill.extensions.SharedTestSuiteExtension
import com.nophasenokill.extensions.TestInvocationListener
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.io.CleanupMode
import org.junit.jupiter.api.io.TempDir
import java.io.File


@ExtendWith(SharedTestSuiteExtension::class, TestInvocationListener::class)
open class FunctionalTest {

    @field:TempDir(factory = JunitTempDirFactory::class, cleanup = CleanupMode.ON_SUCCESS)
    lateinit var projectDir: File

    /*
        Consider adding these in the future if we need them:
            .forwardOutput()
            .forwardStdError()
            .forwardStdOutput()
     */
    val runner: GradleRunner by lazy { GradleRunner.create()
        .withProjectDir(projectDir)
        .withPluginClasspath() }

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

    fun runExpectedSuccessTask(task: String): BuildResult {
        println("Environment for runExpectedSuccessTask is :${runner.environment?.map {  "${it.key}:${it.value}" }}")
        return runner.withArguments(task, "--stacktrace").build()
    }

    fun runExpectedFailureTask(task: String): BuildResult {
        println("Environment for runExpectedFailureTask is :${runner.environment?.map {  "${it.key}:${it.value}" }}")
        return runner.withArguments(task, "--stacktrace").buildAndFail()
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