package com.nophasenokill.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun ScrollableColumn(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .border(2.dp, Color(0xFF6C63FF))  // Border to make it stand out
    ) {
        val verticalScrollState = rememberScrollState()

        // Scroll to the end when content changes
        LaunchedEffect(key1 = verticalScrollState.maxValue) {
            verticalScrollState.animateScrollTo(verticalScrollState.maxValue)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(verticalScrollState)
        ) {
            Column {
                content()
            }
        }

        // Add a wider scrollbar
        VerticalScrollbar(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .width(25.dp)
                .height(25.dp),
            adapter = rememberScrollbarAdapter(verticalScrollState)
        )
    }
}
