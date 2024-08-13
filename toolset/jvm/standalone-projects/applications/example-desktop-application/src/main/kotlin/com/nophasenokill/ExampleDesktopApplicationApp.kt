package com.nophasenokill

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.nophasenokill.components.GradleToolingApiSection
import com.nophasenokill.components.layout.SectionColumn
import com.nophasenokill.components.layout.SectionContainer
import com.nophasenokill.components.layout.SectionRow
import com.nophasenokill.gradle.GradleToolingApi
import com.nophasenokill.theme.AppTheme


@Composable
fun UiContent(content: @Composable () -> Unit) {
    SectionContainer {
        content()
    }
}

fun main() = application {

    val windowState = rememberWindowState(size = DpSize(1200.dp, 900.dp), isMinimized = false, position = WindowPosition(alignment = BiasAlignment(0f, -0.6f)))
    var darkMode by remember { mutableStateOf(false) }

    fun onToggleThemeClick() {
        darkMode = !darkMode
    }

    fun onTaskClicked(value: String) {
        println("Task clicked was: ${value}")
    }

    Window(
        state = windowState,
        onCloseRequest = ::exitApplication,
        title = "Gradle desktop app",
    ) {

        val connector = GradleToolingApi.getConnector("/home/tomga/projects/monorepo/toolset/jvm").connect()

        AppTheme(isDarkMode = darkMode) {
            UiContent {
                SectionRow(horizontalArrangement = Arrangement.SpaceBetween) {
                    SectionColumn(Modifier.align(Alignment.CenterVertically)) {
                        Button(onClick = ::onToggleThemeClick) {
                            Text("Click here to toggle theme")
                        }
                    }
                }

                SectionRow {

                    SectionColumn {
                        Button(onClick = {
                            onTaskClicked("testAll1")
                        }) {
                            Text("Run task: testAll1")
                        }
                    }

                    SectionColumn {
                        GradleToolingApiSection(
                            task = "test",
                            gradleConnector = connector
                        )
                        Button(onClick = {
                            onTaskClicked("testAll3")
                        }) {
                            Text("Run task: testAll3")
                        }
                    }
                    SectionColumn(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
                        Button(onClick = {
                            onTaskClicked("testAll4")
                        }) {
                            Text("Run task: testAll4")
                        }
                    }
                }

                SectionRow {

                    SectionColumn {
                        Button(onClick = {
                            onTaskClicked("testAll5")
                        }) {
                            Text("Run task: testAll5")
                        }
                    }
                }
            }

        }
    }
}