package com.nophasenokill.functionalTest

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.TaskOutcome
import java.io.File

interface GradleTestCapabilities {

    var projectDir: File
    val buildFile: File
    val settingsFile: File

    fun addPluginsById(plugins: List<String>, buildFileToAddPluginsTo: File)
    fun runExpectedSuccessTask(task: String): BuildResult
    fun runExpectedFailureTask(task: String): BuildResult
    fun getTaskOutcome(taskPath: String, result: BuildResult): TaskOutcome
    fun getComparableBuildResultLines(result: BuildResult, trimmedFromTop: Int, trimmedFromBottom: Int): List<String>
    fun getResourceFile(fileNamePath: String): File

}