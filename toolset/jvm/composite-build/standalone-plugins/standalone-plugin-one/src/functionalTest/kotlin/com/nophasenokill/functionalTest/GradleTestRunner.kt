package com.nophasenokill.functionalTest

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import java.io.File

object GradleTestRunner {

    /*
        Consider adding these in the future if we need them:
            .forwardOutput()
            .forwardStdError()
            .forwardStdOutput()
     */

    fun runTask(task: String, projectDir: File): BuildResult {
        return GradleRunner.create()
            .forwardOutput() // required so that we can easily inside of our IDE what was output for each task
            .withProjectDir(projectDir)
            .withArguments(task, "--stacktrace")
            .withPluginClasspath()
            .build()
    }

    fun runTaskWithFailure(task: String, projectDir: File): BuildResult {
        return GradleRunner.create()
            .forwardOutput()
            .withProjectDir(projectDir)
            .withArguments(task, "--stacktrace")
            .withPluginClasspath()
            .buildAndFail()
    }
}