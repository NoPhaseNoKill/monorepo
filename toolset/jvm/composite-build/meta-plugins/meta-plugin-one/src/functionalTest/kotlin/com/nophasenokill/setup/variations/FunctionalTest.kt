

package com.nophasenokill.setup.variations

import com.nophasenokill.setup.junit.extensions.GradleRunnerExtension
import com.nophasenokill.setup.runner.SharedRunnerDetails
import org.gradle.testkit.runner.BuildResult
import org.junit.jupiter.api.extension.ExtendWith
import java.io.File
import java.nio.file.Path

@ExtendWith(GradleRunnerExtension::class)
open class FunctionalTest {

    fun runExpectedSuccessTask(details: SharedRunnerDetails, task: String): BuildResult {
        return details.gradleRunner.withArguments(task, "--warning-mode=all").build()
    }

    fun createFile(file: File) {
        file.createNewFile()
    }

    fun writeText(file: File, textBlock:() -> String) {
        /*
            Putting this outside of the IO context ensures that if the text block
            is computationally expensive we avoid putting this on the IO thread
         */
        val text = textBlock()
        file.writeText(text)
    }

    fun appendText(file: File, textBlock:() -> String) {
        /*
            Putting this outside of the IO context ensures that if the text block
            is computationally expensive we avoid putting this on the IO thread
         */
        val text = textBlock()
        file.appendText(text)
    }

    fun readLines(buildResult: BuildResult): List<String> {
        return buildResult.output.lines()
    }
}

data class TestDirectory(
    val name: String,
    val mainDirectory: Path,
)

