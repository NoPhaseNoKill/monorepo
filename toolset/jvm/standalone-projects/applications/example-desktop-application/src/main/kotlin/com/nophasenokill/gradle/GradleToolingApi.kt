package com.nophasenokill.gradle

import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProjectConnection
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import java.nio.file.Paths

object GradleToolingApi {
    fun runTestsUltraFast(connection: ProjectConnection): String {
        val startTime = System.currentTimeMillis()

        val outputStream = ByteArrayOutputStream()

        connection
            .newBuild()
            .forTasks("testAll")
            .setStandardOutput(PrintStream(outputStream))
            .withArguments("--scan", "--console=plain")
            .run()

        // Measure end time and calculate the duration
        val endTime = System.currentTimeMillis()
        val duration = (endTime - startTime)

        println("Task executed in $duration ms")

        val output = outputStream.toString()
        println("Build Output:\n$output")
        return output

    }

    fun getConnector(): GradleConnector {
        val rootJvmPath = Paths.get("").toAbsolutePath().parent.parent.parent.toString()
        val projectDir = File(rootJvmPath)

        val connector = GradleConnector.newConnector().forProjectDirectory(projectDir)
        return connector
    }

    fun runTestSuite(connector: ProjectConnection): String {
        return runTestsUltraFast(connector)
    }
}