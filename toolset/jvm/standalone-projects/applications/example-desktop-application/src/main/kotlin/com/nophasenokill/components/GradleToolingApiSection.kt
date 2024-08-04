package com.nophasenokill.components

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.nophasenokill.domain.Constants
import com.nophasenokill.gradle.GradleToolingApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun GradleToolingApiSection(modifier: Modifier = Modifier) {
    var isLoadingContent by remember { mutableStateOf(true) }
    var warmedTestRunOutput  by remember { mutableStateOf<String>("") }

    LaunchedEffect (Unit) {
        coroutineScope {
            launch(Dispatchers.IO) {
                val finished = GradleToolingApi.getWarmedTestSuiteResult()
                isLoadingContent = false
                warmedTestRunOutput = finished
            }
        }
    }

    if (isLoadingContent) {
        Loading()
    } else {
        ScrollableColumn {
            Text(
                text = "Content to go undearneath here",
                fontSize = Constants.DEFAULT_FONT_SIZE,
                lineHeight = Constants.DEFAULT_LINE_HEIGHT,
                textAlign = TextAlign.Center,
            )

            Text(
                text = "Has content been loaded: ${!isLoadingContent}",
                fontSize = Constants.DEFAULT_FONT_SIZE,
                lineHeight = Constants.DEFAULT_LINE_HEIGHT,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "Warmed test run output: \nPlease scroll down to see all output\n\n${warmedTestRunOutput}",
                fontSize = Constants.DEFAULT_FONT_SIZE,
                lineHeight = Constants.DEFAULT_LINE_HEIGHT,
                textAlign = TextAlign.Center,
            )
        }
    }
}
