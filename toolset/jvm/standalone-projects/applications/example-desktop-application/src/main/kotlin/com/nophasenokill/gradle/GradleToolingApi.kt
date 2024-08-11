package com.nophasenokill.gradle

import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProjectConnection
import org.gradle.tooling.model.build.BuildEnvironment
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream


object GradleToolingApi {


    fun connectToProject(connector: GradleConnector): ProjectConnection {
        println("CONNECT TO PROJECT connector.connect() ${connector.connect()}")
        return connector.connect()
    }

    fun getConnector(path: String): GradleConnector {
        val connector = GradleConnector.newConnector().forProjectDirectory(File(path))
        return connector
    }

    fun runTask(connection: ProjectConnection, task: String): String {
        val startTime = System.currentTimeMillis()

        val outputStream = ByteArrayOutputStream()

        connection
            .newBuild()
            .forTasks(task)
            .setStandardOutput(PrintStream(outputStream))
            .withArguments("--scan", "--console=plain" )
            .run()

        // Measure end time and calculate the duration
        val endTime = System.currentTimeMillis()
        val duration = (endTime - startTime)

        println("Task executed in $duration ms")

        val buildEnv = connection.getModel(BuildEnvironment::class.java)
        var finalOutput = ""
        finalOutput += "Gradle version: ${buildEnv.gradle.gradleVersion}\n"
        finalOutput += "Java home: ${buildEnv.java.javaHome}\n"
        finalOutput += "Build identifier: ${buildEnv.buildIdentifier.rootDir}\n"
        finalOutput += "JVM args: [ \n${buildEnv.java.jvmArguments.sortedDescending().joinToString(",\n") { it }} \n]\n"
        finalOutput += outputStream.toString() + "\n"

        println("Build Output:\n$finalOutput")
        return finalOutput
    }
}