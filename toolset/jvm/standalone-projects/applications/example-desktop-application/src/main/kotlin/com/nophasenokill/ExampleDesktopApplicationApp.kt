package com.nophasenokill

import androidx.compose.ui.window.application
import com.nophasenokill.components.SplashScreen

fun main() = application {
    val onAppClose = ::exitApplication
    SplashScreen(onAppClose)
}