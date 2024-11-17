package com.nophasenokill.components.designsystem

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nophasenokill.theme.AppTheme


@Composable
fun ListItem(
    title: String,
    body: @Composable () -> Unit
) {
    Row(
        Modifier.padding(AppTheme.sizes.medium)
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(end = AppTheme.sizes.small),
            color = AppTheme.colors.text,
            style = AppTheme.typography.headline3
        )

        body()
    }
}
