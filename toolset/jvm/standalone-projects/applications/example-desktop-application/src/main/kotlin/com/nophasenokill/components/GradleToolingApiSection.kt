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
) {
    var isLoadingContent by remember { mutableStateOf(true) }
    var warmedTestRunOutput by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    var shouldRunTests by remember { mutableStateOf(false) }

    fun onTestStart() {
        isLoadingContent = true
        warmedTestRunOutput = ""
        shouldRunTests = true
    }


    if (isLoadingContent) {
        Loading()

        if(warmedTestRunOutput.isEmpty()) {
            LaunchedEffect(shouldRunTests) {
                val result = withContext(Dispatchers.IO) {
                    GradleToolingApi.runTestSuite(gradleConnector)
                }
                warmedTestRunOutput = result
                isLoadingContent = false
                shouldRunTests = false
            }
        }
    } else {
        Column {
            if(!isLoadingContent) {
                RunTestsButton(
                    onClick = ::onTestStart,
                    text = "Click to run warmed test suite",
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(Constants.DEFAULT_PADDING)
                )

                if(warmedTestRunOutput.isNotEmpty()) {
                    Box(modifier) {
                        Column(
                            modifier = Modifier
                                .verticalScroll(scrollState)
                                .padding(Constants.DEFAULT_PADDING)
                        ) {
                            Text(
                                text = "Warmed test run output:",
                                fontSize = Constants.HEADING_FONT_SIZE,
                                lineHeight = Constants.DEFAULT_LINE_HEIGHT,
                                textAlign = TextAlign.Left,
                            )
                            Text(
                                text = warmedTestRunOutput,
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