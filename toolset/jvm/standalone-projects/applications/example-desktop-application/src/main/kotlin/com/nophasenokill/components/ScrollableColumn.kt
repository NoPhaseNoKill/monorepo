package com.nophasenokill.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nophasenokill.theme.Foreground

@Composable
fun ScrollableColumn(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Foreground)  // Darker background for contrast
            .border(2.dp, Color(0xFF6C63FF))  // Border to make it stand out
    ) {
        val verticalScrollState = rememberScrollState(0)

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
