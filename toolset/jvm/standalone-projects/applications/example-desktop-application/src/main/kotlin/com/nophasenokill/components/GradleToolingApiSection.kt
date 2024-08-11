package com.nophasenokill.components

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.nophasenokill.domain.Constants
import com.nophasenokill.gradle.GradleToolingApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.gradle.tooling.ProjectConnection

@Composable
fun GradleToolingApiSection(
    modifier: Modifier = Modifier,
    gradleConnector: ProjectConnection,
    task: String,
) {
    var isLoadingContent by remember { mutableStateOf(true) }
    var output by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    var shouldRunTask by remember { mutableStateOf(false) }

    fun onTaskStart() {
        isLoadingContent = true
        output = ""
        shouldRunTask = true
    }


    if (isLoadingContent) {
        Loading()

        if(output.isEmpty()) {
            LaunchedEffect(shouldRunTask) {
                val result = withContext(Dispatchers.IO) {
                    GradleToolingApi.runTask(gradleConnector, task)
                }
                output = result
                isLoadingContent = false
                shouldRunTask = false
            }
        }
    } else {
        Column {
            if(!isLoadingContent) {
                RunTaskButton(
                    onClick = ::onTaskStart,
                    text = "Click to run task: ${task} ",
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(Constants.DEFAULT_PADDING)
                )

                if(output.isNotEmpty()) {
                    Box(modifier) {
                        Column(
                            modifier = Modifier
                                .verticalScroll(scrollState)
                                .padding(Constants.DEFAULT_PADDING)
                        ) {
                            Text(
                                text = "Task '${task}' output:",
                                fontSize = Constants.HEADING_FONT_SIZE,
                                lineHeight = Constants.DEFAULT_LINE_HEIGHT,
                                textAlign = TextAlign.Left,
                            )
                            Text(
                                text = output,
                                fontSize = Constants.DEFAULT_FONT_SIZE,
                                lineHeight = Constants.DEFAULT_LINE_HEIGHT,
                                textAlign = TextAlign.Left,
                            )
                        }

                        VerticalScrollbar(
                            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                            adapter = rememberScrollbarAdapter(scrollState)
                        )
                    }
                }
            }
        }
    }
}