package com.nophasenokill.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import com.nophasenokill.domain.Constants
import com.nophasenokill.theme.Background
import com.nophasenokill.theme.Foreground
import com.nophasenokill.windows.ui.common.Theme

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
