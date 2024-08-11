package com.nophasenokill.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nophasenokill.components.splitter.SplitterScreen
import org.gradle.tooling.ProjectConnection

@Composable
fun AppContainer(
    gradleConnector: ProjectConnection,
    task: String
) {

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                SplitterScreen(gradleConnector, task )

            }
        }
    }
}


