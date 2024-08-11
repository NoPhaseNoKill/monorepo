package com.nophasenokill.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import com.nophasenokill.components.splitter.SplitterScreen
import com.nophasenokill.domain.Constants
import kotlinx.coroutines.delay
import org.gradle.tooling.ProjectConnection

@Composable
fun SplashScreen(
    onAppClose: () -> Unit,
    onJavaDirChange: (value: String) -> Unit,
    onTaskChange: (value: String) -> Unit,
    taskName: String,
    projectConnection: ProjectConnection
) {
    var isSplashScreenShowing by remember { mutableStateOf(true) }
    val snackbarState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        delay(700)
        isSplashScreenShowing = false
    }
    if (isSplashScreenShowing) {
        Window(onAppClose, title = "Splash") {}
    } else {

        Window(onAppClose, title = "App") {
            Column {
                if (snackbarState.currentSnackbarData?.visuals?.message?.contains("Valid: true") == true) {
                    val length = requireNotNull(snackbarState.currentSnackbarData?.visuals?.message?.length) {
                        "Expected the length of the snackbar visual message to exist but did not"
                    }

                    val foundString = "Directory:"

                    val start = requireNotNull(snackbarState.currentSnackbarData?.visuals?.message?.lastIndexOf(foundString)) {
                        "Expected to find start index for snackbar state but did not"
                    }
                    val rootDir = requireNotNull (snackbarState.currentSnackbarData?.visuals?.message?.substring(start + foundString.length + 1, length) ) {
                        "Expected to find rootDir for snackbar but did not"
                    }
                    TextField(
                        value = taskName,
                        onValueChange = onTaskChange,
                        label = { Text("Enter Task Name") },
                        modifier = Modifier.fillMaxWidth().padding(Constants.DEFAULT_PADDING)
                    )
                    AppContainer(projectConnection, taskName)

                    onJavaDirChange(rootDir)
                }

                AddBuildButton(snackbarState)
            }

        }
    }
}

