package com.nophasenokill.setup.variations

import com.nophasenokill.setup.junit.extensions.SharedTestSuiteContextKey
import com.nophasenokill.setup.junit.extensions.SharedTestSuiteStore
import com.nophasenokill.setup.runner.SharedRunnerDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import org.gradle.testkit.runner.BuildResult
import org.junit.jupiter.api.extension.ExtensionContext
import java.io.File
import java.nio.file.Path
import kotlin.io.path.createFile

// @ExtendWith(SharedTestSuiteExtension::class, TestInvocationListener::class)
// @ExtendWith(SharedTestSuiteExtension::class)

open class FunctionalTest {

    fun runExpectedSuccessTask(details: SharedRunnerDetails, task: String): BuildResult {
        return details.gradleRunner.withArguments(task, "--warning-mode=all").build()
    }

    suspend fun createFile(file: File) {
        withContext(Dispatchers.IO) {
            file.createNewFile()
        }
    }

    suspend fun writeText(file: File, textBlock:() -> String) {
        /*
            Putting this outside of the IO context ensures that if the text block
            is computationally expensive we avoid putting this on the IO thread
         */
        val text = textBlock()
        withContext(Dispatchers.IO) {
            file.writeText(text)
        }
    }

    suspend fun appendText(file: File, textBlock:() -> String) {
        /*
            Putting this outside of the IO context ensures that if the text block
            is computationally expensive we avoid putting this on the IO thread
         */
        val text = textBlock()
        withContext(Dispatchers.IO) {
            file.appendText(text)
        }
    }

    suspend fun readLines(buildResult: BuildResult): List<String> {
        return withContext(Dispatchers.IO) {
            return@withContext buildResult.output.lines()
        }
    }

}

data class TestDirectory(
    val name: String,
    val mainDirectory: Path,
)

