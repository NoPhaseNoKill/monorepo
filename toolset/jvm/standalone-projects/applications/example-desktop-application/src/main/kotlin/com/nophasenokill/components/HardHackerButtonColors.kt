package com.nophasenokill.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.nophasenokill.theme.HardHackerColor

@Composable
fun HardHackerButtonColors(color: HardHackerColor) {
    Button(
        onClick = { },
        colors = ButtonColors(
            containerColor = color.color,
            contentColor = Color.White,
            disabledContainerColor = color.color,
            disabledContentColor = color.color,
        )
    ) {
        Text("This button: name: ${color.name} ,  hex: ${color.hex}")
    }
}
