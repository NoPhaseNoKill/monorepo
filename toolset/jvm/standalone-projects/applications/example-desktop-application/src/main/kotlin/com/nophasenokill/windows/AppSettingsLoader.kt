package com.nophasenokill.windows

import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import com.nophasenokill.domain.Content
import com.nophasenokill.domain.CoroutineScopeName
import kotlinx.coroutines.*

@Composable
fun AppSettingsLoader(
    scopes: Map<CoroutineScopeName, CoroutineScope>,
    onAppClose: () -> Unit,
    onInitialSplashLoad: (title: String) -> Unit,
    contentAfterLoad: Content
) {
    var isSplashScreenShowing by remember { mutableStateOf(true) }

    LaunchedEffect (Unit) {
        /*
            Loading of settings will go here
         */
        withContext(scopes.getValue(CoroutineScopeName.LOADING_APP_SCOPE).coroutineContext) {
            delay(400)
            isSplashScreenShowing = false
        }
    }

    println("Is active? ${scopes.getValue(CoroutineScopeName.LOADING_APP_SCOPE).coroutineContext.isActive}")

    if (isSplashScreenShowing) {
        Window(onAppClose, title = "Splash") {
            onInitialSplashLoad(window.title)
        }
    } else {
        contentAfterLoad()
    }
}

