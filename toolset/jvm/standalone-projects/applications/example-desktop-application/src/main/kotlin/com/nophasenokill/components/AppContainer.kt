package com.nophasenokill.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nophasenokill.domain.Constants
import org.gradle.tooling.ProjectConnection

@Composable
fun AppContainer(
    gradleConnector: ProjectConnection
) {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AppLayout(modifier = Modifier.padding(Constants.DEFAULT_PADDING), gradleConnector)
        }
    }
}


