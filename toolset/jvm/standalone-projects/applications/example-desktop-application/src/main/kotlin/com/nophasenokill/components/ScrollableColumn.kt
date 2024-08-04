package com.nophasenokill.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nophasenokill.domain.Constants

@Composable
fun ScrollableColumn(
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val verticalScrollState = rememberScrollState(0)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = Constants.DEFAULT_PADDING)
                .verticalScroll(verticalScrollState)
        ) {
            Column {
                content()
            }
        }
    }
}