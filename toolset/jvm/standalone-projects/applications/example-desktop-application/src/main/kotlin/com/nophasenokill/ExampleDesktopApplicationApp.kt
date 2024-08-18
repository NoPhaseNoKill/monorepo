package com.nophasenokill

import androidx.compose.runtime.*
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.nophasenokill.components.DirChooserDialog
import com.nophasenokill.domain.CoroutineScopeName
import com.nophasenokill.domain.GradleConnectorName
import com.nophasenokill.gradle.GradleToolingApi
import com.nophasenokill.windows.AppSettingsLoader
import com.nophasenokill.windows.UIContent
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths


fun main() = application {

    var javaDir by remember { mutableStateOf("") }

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


    fun onJavaDirChange(value: String) {
        if (javaDir != value) {
            println("Changing java dir from: $javaDir, to: $value")
            javaDir = value
            connector = GradleToolingApi.getConnector(javaDir)
            projectConnector = GradleToolingApi.connectToProject(connector)
        }
    }

    DirChooserDialog("Pick a file!", false, { dir -> onJavaDirChange(dir?.absolutePath.toString())})

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

        // Entry point once the settings etc have loaded is here
        Window(onAppClose, title = "App") {
            UIContent(scopes, connectors)
        }

        println("Have set initialAppHasLoaded to true.")

    }

    AppSettingsLoader(scopes, ::onWholeAppClose, ::onInitialLoad) {
        onAppLoad(::onWholeAppClose, onAppLoad)
    }
}


//
// @Composable
// fun WashAGen (
//     onAppClose: () -> Unit,
//     onJavaDirChange: (value: String) -> Unit,
//     onTaskChange: (value: String) -> Unit,
//     taskName: String,
//     projectConnection: ProjectConnection
// ) = application {
//     var isSplashScreenShowing by remember { mutableStateOf(true) }
//     val snackbarState = remember { SnackbarHostState() }
//
//     val connector = GradleToolingApi.getConnector()
//     val projectConnector = connector.connect()
//
//     // fun onAppClose() {
//     //     println("Disconnecting from gradle tooling API connector")
//     //     connector.disconnect()
//     //     ::exitApplication
//     // }
//
//     SplashScreen(onAppClose, ({}), onTaskChange = {}, projectConnector = testAll )
// }

