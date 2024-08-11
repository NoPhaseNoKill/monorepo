package com.nophasenokill

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.application
import com.nophasenokill.components.SplashScreen
import com.nophasenokill.gradle.GradleToolingApi
import java.nio.file.Paths

fun main() = application {

    val defaultCurrentDir = Paths.get("").toAbsolutePath().parent.parent.parent.toString()

    var javaDir by remember { mutableStateOf(defaultCurrentDir) }
    var connector by remember { mutableStateOf(GradleToolingApi.getConnector(javaDir)) }
    var projectConnector by remember { mutableStateOf(GradleToolingApi.connectToProject(connector)) }
    var taskName by remember { mutableStateOf("tasks") }

    fun onJavaDirChange(value: String) {
        if (javaDir != value) {
            println("Changing java dir from: $javaDir, to: $value")
            javaDir = value
            connector = GradleToolingApi.getConnector(javaDir)
            projectConnector = GradleToolingApi.connectToProject(connector)
        }
    }

    fun onTaskChange(value: String) {
        if(taskName != value) {
            taskName = value
        }
    }

    fun onAppClose() {
        println("Disconnecting from gradle tooling API connector")
        connector.disconnect()
        exitApplication()
    }

    SplashScreen(::onAppClose, ::onJavaDirChange, ::onTaskChange, taskName, projectConnector)
}