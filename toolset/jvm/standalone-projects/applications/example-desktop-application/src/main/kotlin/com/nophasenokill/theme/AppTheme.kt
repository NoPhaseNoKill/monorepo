package com.nophasenokill.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val COLOUR_BLACK = Color(0,0,0)

@Composable
fun AppTheme(
    isDarkMode: Boolean,
    content: @Composable () -> Unit
) {
    val lightScheme = lightColorScheme(
        primary = COLOUR_BLACK
    )

    val darkScheme = darkColorScheme(
        primary = COLOUR_BLACK
    )

    val colorScheme =
        if(isDarkMode) darkScheme
        else lightScheme

    val typography = Typography()

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}