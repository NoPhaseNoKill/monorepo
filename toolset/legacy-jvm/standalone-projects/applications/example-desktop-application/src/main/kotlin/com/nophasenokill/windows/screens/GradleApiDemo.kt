package com.nophasenokill.windows.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.nophasenokill.components.Loading
import com.nophasenokill.components.SuccessIcon
import com.nophasenokill.components.base.ToggleBase
import com.nophasenokill.components.button.RunGradleTaskButton
import com.nophasenokill.components.splitter.SplitterScreen
import com.nophasenokill.domain.CoroutineScopeName
import com.nophasenokill.domain.GradleConnectorName
import com.nophasenokill.gradle.GradleToolingApi
import com.nophasenokill.windows.DisplayResults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.gradle.tooling.ProjectConnection

@Composable
fun GradleApiDemo(
    appScopes: Map<CoroutineScopeName, CoroutineScope>,
    connectors: Map<GradleConnectorName, ProjectConnection>,
) {
    appScopes.entries.forEach {
        println("App scopes in gradle api demo: ${it}")
    }

    var iterations by remember { mutableStateOf(0) }
    var loading by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    var screenToShow by remember { mutableStateOf("results") }

    fun onTaskButtonClick(task: String) {
        loading = true
        result = null

        coroutineScope.launch {
            val connector = connectors.getValue(GradleConnectorName.GENERAL)

            try {
                result = withContext(Dispatchers.IO) {
                    GradleToolingApi.runTask(connector, task)
                }
            } catch (e: Exception) {
                result = "Error running task: ${e.message}"
            }
            iterations += 1
            loading = false
        }
    }

    if (loading) {
        Column(modifier = Modifier.fillMaxSize()) {
            Loading()
        }
    } else {
        SplitterScreen(
            connectors.getValue(GradleConnectorName.GENERAL),
            "test",
            contentSectionOne = {
                Column {
                    Row {
                        ToggleBase(onChange = { screenToShow = "" })
                    }
                    Row {
                        Button(
                            onClick = { onTaskButtonClick("test") }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    }
                    Row {
                        RunGradleTaskButton(
                            task = "test",
                            onClick = {
                                screenToShow = "gradleTask"
                                onTaskButtonClick("test")
                            }
                        )
                    }
                }
            },
            contentSectionTwo = {
                result?.let {
                    DisplayResults(it, iterations, loading)
                }
            },
            contentSectionThree = {
                if(result?.contains("BUILD SUCCESSFUL") == true) {

                    Column(Modifier.fillMaxWidth(1f).fillMaxHeight()) {
                        Row {
                            SuccessIcon()
                            SuccessIcon()
                            SuccessIcon()
                            SuccessIcon()
                        }
                    }
                }
            }
        )
    }
}
