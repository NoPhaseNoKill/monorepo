package com.nophasenokill.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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

    // Apply a background color from the theme to the root container
    val backgroundColor = MaterialTheme.colorScheme.background

    /*
        Scroll to the bottom when the output changes
     */
    LaunchedEffect(output) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }
    Box(modifier) {
        Box(
            modifier = modifier
                .background(backgroundColor)
                .fillMaxWidth(0.75f)
        ) {
            if (isLoadingContent) {
                Loading()

                if (output.isEmpty()) {
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
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp) // Add spacing between elements
                ) {
                    RunTaskButton(
                        onClick = ::onTaskStart,
                        text = "Click to run task: $task",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(Constants.DEFAULT_PADDING)
                    )

                    if (output.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .weight(1f) // Allow this Box to expand and take up available space
                                .padding(Constants.DEFAULT_PADDING)
                            // .fillMaxWidth(0.75f) // Take up 75% of the screen width,
                        ) {
                            Column(
                                modifier = Modifier
                                    .verticalScroll(scrollState)
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Task '$task' output:",
                                    fontSize = Constants.HEADING_FONT_SIZE,
                                    lineHeight = Constants.DEFAULT_LINE_HEIGHT,
                                    textAlign = TextAlign.Left,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                CodeBlock(modifier = Modifier, code = buildAnnotatedString { append(output) })
                            }

                            VerticalScrollbar(
                                modifier = Modifier.align(Alignment.CenterEnd),
                                adapter = rememberScrollbarAdapter(scrollState)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        SuccessIcon(modifier)
                    }
                }
            }
        }
    }
}