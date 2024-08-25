package com.nophasenokill

import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.nophasenokill.components.DirChooserDialog
import com.nophasenokill.components.designsystem.compose.NewTheme
import com.nophasenokill.domain.CoroutineScopeName
import com.nophasenokill.domain.GradleConnectorName
import com.nophasenokill.gradle.GradleToolingApi
import com.nophasenokill.windows.AppSettingsLoader
import com.nophasenokill.windows.UIContent
import java.io.File


fun main() = application {

    val state = rememberWindowState(WindowPlacement.Floating)
    var javaDir by remember { mutableStateOf("") }

    val APP_NAME = "NoPhaseNoKill Gradle Build Tool"

    // val baseDir = Paths.get("").toAbsolutePath().toString()
    // val defaultCurrentDir = "$baseDir/src/main/resources/example-in-app-project"


    val blockingNetworkRequests = rememberCoroutineScope()
    val blockingFileIo = rememberCoroutineScope()

    val scopes = mapOf(
        CoroutineScopeName.LOADING_APP_SCOPE to blockingNetworkRequests,
        CoroutineScopeName.BLOCKING_FILE_IO to blockingFileIo,
    )


    var initialAppHasLoaded by remember { mutableStateOf(false) }
    var connector by remember { mutableStateOf(GradleToolingApi.getConnector(javaDir)) }
    var projectConnector by remember { mutableStateOf(GradleToolingApi.connectToProject(connector)) }
    var taskName by remember { mutableStateOf("tasks") }

    val connectors = mapOf(
        GradleConnectorName.GENERAL to projectConnector
    )


    fun onJavaDirChange(value: File) {
        println("Java path is: ${javaDir}, absolute is: ${value.absolutePath}")
        if (javaDir != value.absolutePath) {
            println("Changing java dir from: $javaDir, to: $value")
            javaDir = value.absolutePath
            connector = GradleToolingApi.getConnector(javaDir)
            projectConnector = GradleToolingApi.connectToProject(connector)
        }
    }


    fun onTaskChange(value: String) {
        if (taskName != value) {
            taskName = value
        }
    }

    fun onWholeAppClose() {
        println("Whole app window closing")
        println("Disconnecting from gradle tooling API connector")
        connector.disconnect()
        println("Disconnected from gradle tooling API connector")
        exitApplication()
        println("Whole app window closed")
    }

    val onAppLoad = {
        println("App is about to be loaded. Setting initialAppHasLoaded to true")
        initialAppHasLoaded = true
        println("initialAppHasLoaded has been set to true")
    }

    fun onInitialLoad(title: String) {
        println("Screen title of initial screen is: ${title}")
    }

    @Composable
    fun onAppLoad(onAppClose: () -> Unit, onLoad: () -> Unit) {
        onLoad()
        println("Setting initialAppHasLoaded to true. This should not happen until splash screen is closed")

        val windowState = rememberWindowState(size = DpSize(1800.dp, 1200.dp))

            LaunchedEffect(Unit) {
                println("$APP_NAME started!")
            }

            // Entry point once the settings etc have loaded is here
            Window(onAppClose, title = "App") {

                // NewTheme({})

                UIContent(scopes, connectors) {
                    DirChooserDialog("Pick a file!", false) { dir ->
                        if (dir != null) {
                            onJavaDirChange(dir)
                        }
                    }
                }
            }
        println("Have set initialAppHasLoaded to true.")

    }

    AppSettingsLoader(scopes, ::onWholeAppClose, ::onInitialLoad) {
        onAppLoad(::onWholeAppClose, onAppLoad)
    }
}

