package com.nophasenokill.theme

import androidx.compose.ui.graphics.Color

fun HexColor(hex: String): Color {
    val cleanHex = hex.removePrefix("#")
    val colorLong = cleanHex.toLong(16)

    return when (cleanHex.length) {
        6 -> Color(colorLong or 0x00000000FF000000) // Assuming full opacity if only RGB is provided
        8 -> Color(colorLong) // ARGB format
        else -> throw IllegalArgumentException("Invalid color format")
    }
}
