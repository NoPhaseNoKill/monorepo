package com.nophasenokill.gradle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import org.gradle.api.JavaVersion
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProjectConnection
import org.gradle.tooling.model.build.BuildEnvironment
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import kotlin.system.measureTimeMillis


object GradleToolingApi {


    fun connectToProject(connector: GradleConnector): ProjectConnection {
        fun connectToProject(): ProjectConnection {
            return connector.connect()
        }
        val connection = connectToProject()
        println("Connection to project connector.connect() ${connection}")

        println("WARNING WE ARE ABOUT TO BLOCK THREAD for ${connection.model(BuildEnvironment::class.java)} Current time: ${System.currentTimeMillis()}")
        val buildEnvironment = connection.model(BuildEnvironment::class.java).get()
        println("THREAD UNBLOCKED for ${connection.model(BuildEnvironment::class.java)}. Current time: ${System.currentTimeMillis()}")

        println("""
            Build environment with project connection ${connection} is:
            
                buildIdentifier: ${buildEnvironment.buildIdentifier}
                gradleUserHome: ${buildEnvironment.gradle.gradleUserHome}
                gradleVersion: ${buildEnvironment.gradle.gradleVersion}
                javaHome: ${buildEnvironment.java.javaHome}
                jvmArguments: ${buildEnvironment.java.jvmArguments}
                
        """.trimIndent())
        return connection
    }

    fun getConnector(path: String): GradleConnector {
        val connector = GradleConnector.newConnector().forProjectDirectory(File(path))
        return connector
    }

    suspend fun runTask(connection: ProjectConnection, task: String): String {
        val startTime = System.currentTimeMillis()

        val outputStream = ByteArrayOutputStream()

        val instrumentedJavaVersionFromGradle =
            connection.getModel(BuildEnvironment::class.java).java.javaHome.absolutePath
        try {
            val buildModelVersion = connection.getModel(BuildEnvironment::class.java).java.javaHome.absolutePath
            val version =
                if (!instrumentedJavaVersionFromGradle.isNullOrEmpty()) instrumentedJavaVersionFromGradle
                else buildModelVersion

            if (version != null) {
                println("instrumentedJavaVersionFromGradle is: ${instrumentedJavaVersionFromGradle} ")
                println(
                    "connection.getModel(BuildEnvironment::class.java).java.javaHome.absolutePath  is: ${
                        connection.getModel(
                            BuildEnvironment::class.java
                        ).java.javaHome.absolutePath
                    } "
                )

                connection
                    .newBuild()
                    .forTasks(task)
                    .setStandardOutput(PrintStream(outputStream))
                    .withArguments("--scan", "--console=plain")
                    .run()

                println("Has it finished???")

                // Measure end time and calculate the duration
                val endTime = System.currentTimeMillis()
                val duration = (endTime - startTime)

                println("Task executed in $duration ms")

                val buildEnv = connection.getModel(BuildEnvironment::class.java)
                var finalOutput = ""
                finalOutput += "Gradle version: ${buildEnv.gradle.gradleVersion}\n"
                finalOutput += "Java home: ${buildEnv.java.javaHome}\n"
                finalOutput += "Build identifier: ${buildEnv.buildIdentifier.rootDir}\n"
                finalOutput += "JVM args: [ \n${
                    buildEnv.java.jvmArguments.sortedDescending().joinToString(",\n") { it }
                } \n]\n"
                finalOutput += outputStream.toString() + "\n"

                println("Build Output:\n$finalOutput")
                return finalOutput
            } else {
                return "Whoops the request dun goofed"
            }

        } catch (e: Exception) {
            println("Exception was: ${e}")
            throw e
        }
    }
}
