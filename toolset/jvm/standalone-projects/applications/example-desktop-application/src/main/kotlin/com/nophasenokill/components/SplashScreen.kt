package com.nophasenokill.components

import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onAppClose: () -> Unit) {
    var isSplashScreenShowing by remember { mutableStateOf(true) }
    LaunchedEffect (Unit) {
        delay(700)
        isSplashScreenShowing = false
    }
    if (isSplashScreenShowing) {
        Window(onAppClose, title = "Splash") {}
    } else {
        Window(onAppClose, title = "App") {
            AppContainer()
        }
    }
}

