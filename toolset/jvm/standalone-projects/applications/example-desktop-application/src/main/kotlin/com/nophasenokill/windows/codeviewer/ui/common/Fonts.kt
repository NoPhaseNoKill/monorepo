package com.nophasenokill.windows.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font

object Fonts {
    @Composable
    fun jetbrainsMono() = FontFamily(
        Font(resource = "fonts/JetBrainsMono-Regular.ttf", weight = FontWeight.Normal, style = FontStyle.Normal),
        Font(resource = "fonts/JetBrainsMono-Italic.ttf", weight = FontWeight.Normal, style = FontStyle.Italic),
        Font(resource = "fonts/JetBrainsMono-Bold.ttf", weight = FontWeight.Bold, style = FontStyle.Normal),
        Font(resource = "fonts/JetBrainsMono-BoldItalic.ttf", weight = FontWeight.Bold, style = FontStyle.Italic),
        Font(resource = "fonts/JetBrainsMono-ExtraBold.ttf", weight = FontWeight.ExtraBold, style = FontStyle.Normal),
        Font(resource = "fonts/JetBrainsMono-ExtraBoldItalic.ttf", weight = FontWeight.ExtraBold, style = FontStyle.Italic),
        Font(resource = "fonts/JetBrainsMono-Medium.ttf", weight = FontWeight.Medium, style = FontStyle.Normal),
        Font(resource = "fonts/JetBrainsMono-MediumItalic.ttf", weight = FontWeight.Medium, style = FontStyle.Italic)
    )
}
