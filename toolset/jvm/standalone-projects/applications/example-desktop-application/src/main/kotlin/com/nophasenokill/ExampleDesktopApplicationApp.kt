package com.nophasenokill

import androidx.compose.ui.window.application
import com.nophasenokill.components.SplashScreen
import com.nophasenokill.gradle.GradleToolingApi

fun main() = application {
    val connector = GradleToolingApi.getConnector()
    val projectConnector = connector.connect()

    fun onAppClose() {
        println("Disconnecting from gradle tooling API connector")
        connector.disconnect()
        exitApplication()
    }

    SplashScreen(::onAppClose, projectConnector)
}