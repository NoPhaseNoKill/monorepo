package com.nophasenokill.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nophasenokill.domain.Constants
import org.gradle.tooling.ProjectConnection

@Composable
fun AppLayout(modifier: Modifier = Modifier, gradleConnector: ProjectConnection) {

    var runTests by remember { mutableStateOf(false) }


    Column {
        if(runTests) {
            GradleToolingApiSection(modifier, gradleConnector)
        } else {
            RunTestsButton(
                onClick = { runTests = !runTests },
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(Constants.DEFAULT_PADDING)
            )
        }
    }
}