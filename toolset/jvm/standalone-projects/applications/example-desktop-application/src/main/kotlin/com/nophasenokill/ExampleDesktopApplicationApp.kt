package com.nophasenokill

import androidx.compose.ui.window.application


fun main() = application {
    val onAppClose = ::exitApplication
    SplashScreen(onAppClose)
}

