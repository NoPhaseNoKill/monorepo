package com.nophasenokill.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.window.Window
import com.nophasenokill.domain.Constants
import com.nophasenokill.gradle.GradleToolingApi
import kotlinx.coroutines.*
import org.gradle.tooling.ProjectConnection

@Composable
fun SplashScreen(
    taskName: String,
    projectConnection: ProjectConnection,
    onAppClose: () -> Unit,
    onJavaDirChange: (value: String) -> Unit,
    onTaskChange: (value: String) -> Unit,
) {
    var isSplashScreenShowing by remember { mutableStateOf(true) }
    val snackbarState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var output by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(4300)
        isSplashScreenShowing = false
    }

    suspend fun getResult(scope: CoroutineScope) {
        withContext(scope.coroutineContext) {
            scope.launch {
                val result = GradleToolingApi.runTask(projectConnection, "test")
                output = result
            }
        }
    }

    suspend fun fetchAndAwaitResult(scope: CoroutineScope) {
        withContext(scope.coroutineContext) {
            val awaited = scope.launch {
                loading = true
                getResult(scope)
                loading = false
            }
            awaited.join()
        }
    }

    if(!isSplashScreenShowing) { Window(onAppClose, title = "Splash") {

    }}

    else {
        Window(onAppClose, title = "App") {

            Column(Modifier.fillMaxWidth()) {

                Button(onClick = {}) {
                    Text("Content is: ${output}. Will start as null, and turn to 3 after it loads")
                }

                Button(onClick = {}) {
                    Text("isSplashScreenShowing: ${isSplashScreenShowing}")
                }

                Button(onClick = {}) {
                    Text("Is loading?")

                    Button(onClick = {}) {
                        Text(text = buildAnnotatedString { append(isSplashScreenShowing.toString()) }, color = if(isSplashScreenShowing) Color.Red else Color.Green)
                    }
                }

                snackbarState.currentSnackbarData?.visuals?.message?.let { message ->
                    if (message.contains("Valid: true")) {
                        val rootDir = message.substringAfter("Directory:").trim()
                        TextField(
                            value = taskName,
                            onValueChange = onTaskChange,
                            label = { Text("Enter Task Name") },
                            modifier = Modifier.fillMaxWidth().padding(Constants.DEFAULT_PADDING)
                        )
                        onJavaDirChange(rootDir)
                    }
                }

                Column {
                    AddBuildButton(snackbarState)
                }


                Column {

                    if (!isSplashScreenShowing) {

                        LaunchedEffect(Unit) {
                            val awaited = scope.launch {
                                fetchAndAwaitResult(scope)
                            }
                            awaited.join()
                        }.also {

                            Button(onClick = {}) {
                                Text("Value of something being initialized after the result came back should be false. Value: ${loading}")
                            }
                        }


                    }
                }

            }

        }
    }
}

