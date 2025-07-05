package com.nophasenokill

import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.nophasenokill.domain.CoroutineScopeName
import com.nophasenokill.domain.GradleConnectorName
import com.nophasenokill.gradle.GradleToolingApi
import com.nophasenokill.windows.AppSettingsLoader
import com.nophasenokill.windows.UIContent
import org.gradle.tooling.ProjectConnection


fun main() = application {

    var javaDir by remember { mutableStateOf("") }

    val blockingNetworkRequests = rememberCoroutineScope()
    val blockingFileIo = rememberCoroutineScope()

    val scopes = mapOf(
        CoroutineScopeName.LOADING_APP_SCOPE to blockingNetworkRequests,
        CoroutineScopeName.BLOCKING_FILE_IO to blockingFileIo,
    )


    var initialAppHasLoaded by remember { mutableStateOf(false) }
    var connector by remember { mutableStateOf(GradleToolingApi.getConnector(javaDir)) }
    var projectConnector by remember { mutableStateOf(GradleToolingApi.connectToProject(connector)) }

    var connectors: Map<GradleConnectorName, ProjectConnection> by remember {
        mutableStateOf(
            mapOf(GradleConnectorName.GENERAL to projectConnector)
        )
    }


    fun onJavaDirChange(value: String) {

        if (javaDir != value) {
            println("Changing java dir from: $javaDir, to: $value")
            javaDir = value
            connector.disconnect()
            connector = GradleToolingApi.getConnector(javaDir)
            projectConnector = GradleToolingApi.connectToProject(connector)
            connectors = connectors.toMutableMap().apply {
                this[GradleConnectorName.GENERAL] = projectConnector
            }
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
            UIContent(scopes, connectors, { value -> onJavaDirChange(value) })
        }



        println("Have set initialAppHasLoaded to true.")

    }

    AppSettingsLoader(scopes, ::onWholeAppClose, ::onInitialLoad) {
        onAppLoad(::onWholeAppClose, onAppLoad)
    }
}
