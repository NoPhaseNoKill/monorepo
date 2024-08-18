package com.nophasenokill.components.button

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nophasenokill.domain.Constants

@Composable
fun RunGradleTaskButton(
    task: String,
    onClick: (task: String) -> Unit
) {
    Button(onClick = {
        onClick(task)
    }) {
        Box (
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(top = Constants.DEFAULT_PADDING),
        ) {

            Text("Click me to run gradle task: ${task}")
        }
    }
}
