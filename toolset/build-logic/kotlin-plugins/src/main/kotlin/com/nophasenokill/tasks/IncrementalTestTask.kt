package com.nophasenokill.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileType
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.work.ChangeType
import org.gradle.work.FileChange
import org.gradle.work.Incremental
import org.gradle.work.InputChanges
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.function.Consumer
import kotlin.io.*
import kotlin.io.path.absolutePathString


abstract class IncrementalTestTask : DefaultTask() {
    @get:InputDirectory
    @get:Incremental
    @get:Optional
    abstract val testSrcDir: DirectoryProperty

    @get:OutputDirectory
    abstract val testResultsDir: DirectoryProperty

    @TaskAction
    fun executeTests(inputChanges: InputChanges) {

        val srcDir: File = testSrcDir.get().getAsFile()

        inputChanges.getFileChanges(testSrcDir).forEach(Consumer { change: FileChange ->
            val file: File = change.file
            if (change.fileType == FileType.DIRECTORY) return@Consumer
            try {

                val targetFile: File = File(testResultsDir.get().asFile, change.normalizedPath)
                if (change.changeType == ChangeType.REMOVED) {
                    // Remove the corresponding test result file when the test is removed
                    if (targetFile.exists()) {
                        targetFile.delete()
                        logger.lifecycle("Deleted test result for: " + change.file.name)
                    }
                } else {

                    srcDir.let { sourceFile ->
                        file.copyTo(targetFile, true)
                    }

                    runTestsOnFile(file)
                }
            } catch (e: IOException) {
                throw RuntimeException("Failed to process test file changes.", e)
            }
        })
    }

    private fun runTestsOnFile(testFile: File) {
        logger.lifecycle("Running tests for: " + testFile.getName())

        // Simulating running tests for each file. In real life, you'd use a test runner (e.g., JUnit).
        // For example, if using JUnit:
        // JUnitCore.runClasses(Class.forName(testFile.getNameWithoutExtension()));

        // Here, we'll just simulate running tests for each file.
        // Replace this with actual test execution logic.
        println("Simulated test run for: " + testFile.getName())
    }
}
