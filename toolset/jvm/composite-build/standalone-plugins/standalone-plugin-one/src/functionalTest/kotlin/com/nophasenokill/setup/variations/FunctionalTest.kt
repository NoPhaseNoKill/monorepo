package com.nophasenokill.setup.variations

import com.nophasenokill.setup.runner.SharedRunnerDetails
import kotlinx.coroutines.*
import org.gradle.api.logging.Logging
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import java.io.File
import java.nio.file.Files
import kotlin.io.path.createFile

fun sharedRunnerDirLazy(): Lazy<File> {
    return lazy { Files.createTempDirectory("shared-runner-dir").toFile() }
}

object FunctionalTest {


    val INDENT: String
        get() = "                "

    suspend fun<T> launchAsyncWork(block: suspend () -> T) {
        block()
    }

    suspend fun<T> getAsyncResult(block: suspend () -> T): T {
        return block()
    }


    @OptIn(InternalCoroutinesApi::class)
    suspend fun runExpectedSuccessTask(runner: GradleRunner, task: String): BuildResult = coroutineScope {
        withContext(context = coroutineContext.newCoroutineContext(Dispatchers.IO)) {
            runner
                .withArguments(
                    task,
                    "--warning-mode=all",
                ).build()
        }
}

    @OptIn(InternalCoroutinesApi::class)
    suspend fun runExpectedFailureTask(details: SharedRunnerDetails, task: String): BuildResult = coroutineScope  {
        withContext(context = coroutineContext.newCoroutineContext(Dispatchers.IO)) {
            details.gradleRunner.withArguments(task, "--warning-mode=all").buildAndFail()
        }
    }

    fun getTaskOutcome(taskPath: String, result: BuildResult): TaskOutcome {
        try {
            return requireNotNull(result.task(taskPath)?.outcome)
        } catch (e: Exception) {
            Logging.getLogger("SharedAppExtension").debug("Task outcome could not be found for task path '${taskPath}'. Exception was ${e.message}")
            throw e
        }
    }

    fun getComparableBuildResultLines(result: BuildResult, trimmedFromTop: Int, trimmedFromBottom: Int): List<String> {
        val removeStartOfFile: List<String> =  result.output.lines().subList(trimmedFromTop, result.output.lines().size)
        return removeStartOfFile.subList(0, removeStartOfFile.size - trimmedFromBottom)
    }


    /*
        This ensures the test is relocatable for cache, as the file should always be relative
     */

    suspend fun getResourceFile(fileNamePath: String): File {
        return getAsyncResult {
            val classLoader = Thread.currentThread().contextClassLoader
            val resourceURL = requireNotNull(
                classLoader.getResource(fileNamePath)
            )
            val file = File(resourceURL.toURI())
            file.useLines {  }
            file
        }
    }

    fun addPluginsById(plugins: List<String>, buildFileToAddPluginsTo: File) {

        val formattedPlugins = plugins.joinToString(prefix = INDENT, separator = "\n$INDENT") {
            "id(\"$it\")"
        }

        val text = """
            plugins {
$formattedPlugins
            }
        """.trimIndent()

        buildFileToAddPluginsTo.writeText(text)
    }

    suspend fun createGradleRunner(runner: GradleRunner): SharedRunnerDetails {
        return getAsyncResult {
            val projectDir  = Files.createTempDirectory("shared-runner-dir")

            val buildFile = projectDir.resolve("build.gradle.kts")
            buildFile.createFile()
            val settingsFile = projectDir.resolve("settings.gradle.kts")

            SharedRunnerDetails(
                projectDir.toFile(),
                buildFile.toFile(),
                settingsFile.toFile(),
                runner
                    .withProjectDir(projectDir.toFile())
                    .withPluginClasspath()
            )
        }

    }
}


sealed class CoroutineType
data object BlockingType: CoroutineType()
data object NonBlockingType: CoroutineType()

interface Provider<T> {
    fun get(): T
}

class LazyProvider<T>(private val lazyValue: () -> Lazy<T>) : Provider<T> {
    private var value: T? = null
    private var isComputed = false

    override fun get(): T {
        if(!isComputed) {
            value = lazyValue().value
            isComputed = true
        }
        return requireNotNull(value)
    }
}

fun <T> lazyProvider(initializer: T): LazyProvider<T> {
    return LazyProvider { lazyOf(initializer) }
}

