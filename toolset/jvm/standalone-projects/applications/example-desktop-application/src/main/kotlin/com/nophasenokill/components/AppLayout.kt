package com.nophasenokill.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun AppLayout(modifier: Modifier = Modifier) {

    var runTests by remember { mutableStateOf(false) }

    if(runTests) {
        GradleToolingApiSection(modifier)
    } else {
        Column {
            Button (onClick = { runTests = true }) {
                Text(text = "Click to run tests")
            }
        }
    }
}