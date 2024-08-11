package com.nophasenokill.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun RunTestsButton(onClick: () -> Unit, text: String = "Click to run tests", modifier: Modifier = Modifier) {
    Button(
        onClick = { onClick() },
        modifier = modifier
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
        )
    }
}