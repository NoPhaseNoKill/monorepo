package com.nophasenokill.windows

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.nophasenokill.components.AppLayout
import com.nophasenokill.components.Loading
import com.nophasenokill.components.Page
import com.nophasenokill.components.base.ToggleBase
import com.nophasenokill.components.button.RunGradleTaskButton
import com.nophasenokill.components.splitter.*
import com.nophasenokill.domain.Constants
import com.nophasenokill.domain.CoroutineScopeName
import com.nophasenokill.domain.GradleConnectorName
import com.nophasenokill.gradle.GradleToolingApi
import com.nophasenokill.theme.AppTheme
import kotlinx.coroutines.*
import org.gradle.tooling.ProjectConnection

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
fun UIContent(appScopes: Map<CoroutineScopeName, CoroutineScope>, connectors: Map<GradleConnectorName, ProjectConnection>) {

    println("Is active? ${appScopes.getValue(CoroutineScopeName.LOADING_APP_SCOPE).coroutineContext.isActive}")

    var iterations by remember { mutableStateOf(0) }
    var loading by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    var screenToShow by remember { mutableStateOf("results") }

    fun onTaskButtonClick(task: String) {
        // Set loading to true immediately when the button is clicked so it initiates spinner
        loading = true
        coroutineScope.launch {
            val connector = connectors.getValue(GradleConnectorName.GENERAL)

            try {
                // Execute the Gradle task in a background thread
                result = withContext(Dispatchers.IO) {
                    GradleToolingApi.runTask(connector, task)
                }
            } catch (e: Exception) {
                // Handle errors by setting the result to the error message
                result = "Error running task: ${e.message}"
            }
            iterations += 1
            // Set loading to false after the task completes
            loading = false
        }
    }

    AppTheme {

        // val connector = connectors.getValue(GradleConnectorName.GENERAL)
        // SplitterScreen(connector, "test")

        Page {
            if(result == null) {
                Column(modifier = Modifier.fillMaxSize()) {

                    RunGradleTaskButton(
                        task = "test",
                        onClick = { task -> onTaskButtonClick(task) }
                    )

                    // Display the loading state while the task is running
                    if(loading) {
                        Loading()
                    }
                }

            } else {
                result?.let {
                    DisplayResults(it, iterations, loading)
                }
            }
        }

        Page {
            if(result == null) {
                Column(modifier = Modifier.fillMaxSize()) {

                    RunGradleTaskButton(
                        task = "test",
                        onClick = { task -> onTaskButtonClick(task) }
                    )

                    // Display the loading state while the task is running
                    if(loading) {
                        Loading()
                    }
                }
            } else {

                fun onClickToggle() {
                    if(screenToShow == "results") {
                        screenToShow = "splitter"
                    } else if(screenToShow == "splitter") {
                        screenToShow = "results"
                    } else {
                        throw Exception("Could not find screen")
                    }
                }

                SplitterScreen(
                    connectors.getValue(GradleConnectorName.GENERAL),
                    "test",
                    contentSectionOne = { ToggleBase(onChange = { onClickToggle() }) },
                    contentSectionTwo = {
                        result?.let {
                            DisplayResults(it, iterations, loading)
                        }
                    },
                    contentSectionThree = {
                        RunGradleTaskButton(
                            task = "test",
                            onClick = { task -> onTaskButtonClick(task) }
                        )
                    }
                )
                //
                // Row(modifier = Modifier.fillMaxSize()) {
                //     ToggleBase(onChange = { onClickToggle() })
                // }
                //
                // Row {
                //     result?.let {
                //         DisplayResults(it, iterations, loading)
                //     }
                //
                // }


            }
        }
    }
}
