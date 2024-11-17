package com.nophasenokill.components.designsystem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nophasenokill.theme.AppTheme

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = AppTheme.colors.primary,
            contentColor = AppTheme.colors.textReverse
        ),
        contentPadding = PaddingValues(
            start = AppTheme.sizes.large,
            top = AppTheme.sizes.medium,
            end = AppTheme.sizes.large,
            bottom = AppTheme.sizes.medium
        )
    ) {
        Text(
            text = text,
            style = AppTheme.typography.button
        )
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            contentColor = AppTheme.colors.primary
        ),
        border = BorderStroke(1.dp, AppTheme.colors.primary),
        contentPadding = PaddingValues(
            start = AppTheme.sizes.large,
            top = AppTheme.sizes.medium,
            end = AppTheme.sizes.large,
            bottom = AppTheme.sizes.medium
        )
    ) {
        Text(
            text = text,
            style = AppTheme.typography.button
        )
    }
}
