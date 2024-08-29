package com.nophasenokill.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nophasenokill.theme.AppTheme

@Composable
fun SuccessIcon(modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Default.Check,
        contentDescription = "Success",
        tint = AppTheme.colors.success,
        modifier = modifier.size(48.dp)
    )
}
