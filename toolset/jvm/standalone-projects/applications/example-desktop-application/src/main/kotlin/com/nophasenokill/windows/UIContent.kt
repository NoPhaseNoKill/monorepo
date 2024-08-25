package com.nophasenokill.windows

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.nophasenokill.components.Loading
import com.nophasenokill.components.Page
import com.nophasenokill.components.base.ToggleBase
import com.nophasenokill.components.button.RunGradleTaskButton
import com.nophasenokill.components.designsystem.compose.HomeSections
import com.nophasenokill.components.designsystem.compose.NewTheme
import com.nophasenokill.components.designsystem.compose.ui.DlsTheme
import com.nophasenokill.components.splitter.ExperimentalSplitPaneApi
import com.nophasenokill.components.splitter.SplitterScreen
import com.nophasenokill.domain.CoroutineScopeName
import com.nophasenokill.domain.GradleConnectorName
import com.nophasenokill.gradle.GradleToolingApi
import com.nophasenokill.theme.AppTheme
import com.nophasenokill.theme.Foreground
import kotlinx.coroutines.*
import org.gradle.tooling.ProjectConnection

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
fun UIContent(
    appScopes: Map<CoroutineScopeName, CoroutineScope>,
    connectors: Map<GradleConnectorName, ProjectConnection>,
    onJavaDirChange: @Composable () -> Unit
) {

    println("Is active? ${appScopes.getValue(CoroutineScopeName.LOADING_APP_SCOPE).coroutineContext.isActive}")

    var iterations by remember { mutableStateOf(0) }
    var loading by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    var screenToShow by remember { mutableStateOf("results") }

    fun onTaskButtonClick(task: String) {
        // Set loading to true immediately when the button is clicked so it initiates spinner
        loading = true
        result = null

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


        Page {
            if (loading) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Loading(Modifier.fillMaxWidth(1f).fillMaxHeight(1f).background(Foreground))
                }

                onJavaDirChange()
            } else {
                SplitterScreen(
                    connectors.getValue(GradleConnectorName.GENERAL),
                    "test",
                    contentSectionOne = {
                        NewTheme() {}
                    },

                    contentSectionTwo = {
                        result?.let {
                            DisplayResults(it, iterations, loading)
                        }
                    },
                    contentSectionThree = {
                        if (!loading) {
                            RunGradleTaskButton(
                                task = "test",
                                onClick = { task ->
                                    onTaskButtonClick(HomeSections.GradleTask.label)
                                }
                            )
                        }
                    }
                )

                Page {
                    if (result == null) {
                        Column(modifier = Modifier.fillMaxSize()) {

                            if (loading) {
                                Loading()
                            } else {
                                RunGradleTaskButton(
                                    task = "test",
                                    onClick = { task -> onTaskButtonClick(task) }
                                )
                            }
                        }

                    } else {
                        result?.let {
                            DisplayResults(it, iterations, loading)
                        }
                    }
                }

                Page {
                    if (result == null) {
                        Column(modifier = Modifier.fillMaxSize()) {

                            RunGradleTaskButton(
                                task = "test",
                                onClick = { task -> onTaskButtonClick(task) }
                            )
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
                                            onClick = { task ->
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

                                BottomAppBar(
                                    backgroundColor = DlsTheme.colors.primary
                                ) {

                                    val (currentSection, setCurrentSection) = rememberSaveable {
                                        mutableStateOf(HomeSections.Style)
                                    }

                                    val navItems = HomeSections.entries
                                    navItems.forEach { section ->
                                        BottomNavigationItem(
                                            selected = currentSection == section,
                                            onClick = { setCurrentSection(section) },
                                            icon = { Icon(imageVector = section.icon, contentDescription = null) },
                                            label = { Text(section.label) }
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
            // Box(
            //     modifier = Modifier
            //         .fillMaxSize()
            //         .background(Foreground) // Darker background for contrast
            // ) {
            //
            // }
        }
    }
}
