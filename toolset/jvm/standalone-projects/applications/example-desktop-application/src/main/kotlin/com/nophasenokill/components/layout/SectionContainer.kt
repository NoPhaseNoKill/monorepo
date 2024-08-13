package com.nophasenokill.components.layout

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nophasenokill.domain.Constants


@Composable
fun SectionContainer(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Constants.DEFAULT_PADDING)
    ) {
        content()
    }
}

@Composable
fun SectionRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical =  Alignment.Top,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        modifier = modifier
            .fillMaxWidth()
            .padding(Constants.DEFAULT_PADDING)
    ) {
        content()
    }
}


@Composable
fun SectionColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier.padding(Constants.DEFAULT_PADDING)
    ) {
        content()
    }
}