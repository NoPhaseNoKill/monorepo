package com.nophasenokill.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import kotlinx.coroutines.launch

@Composable
fun AddBuildButton(snackbarState: SnackbarHostState) {

    val ADD_BUILD_HELP_TEXT = "Choose a Gradle build directory"

    val scope = rememberCoroutineScope()
    var isDirChooserOpen by remember { mutableStateOf(false) }
    if (isDirChooserOpen) {
        DirChooserDialog(
            helpText = ADD_BUILD_HELP_TEXT,
            onDirChosen = { rootDir ->
                isDirChooserOpen = false
                if (rootDir == null) {
                    scope.launch { snackbarState.showSnackbar("No build selected") }
                } else {
                    val valid = rootDir.listFiles { file ->
                        file.name.contains("settings.gradle")
                    }?.isNotEmpty() ?: false
                    if (!valid) {
                        scope.launch { snackbarState.showSnackbar("Directory is not a Gradle build!") }
                    } else {
                        scope.launch { snackbarState.showSnackbar("Valid: true. Directory: $rootDir") }
                    }
                }
            }
        )
    }
    PlainTextTooltip(ADD_BUILD_HELP_TEXT) {
        ExtendedFloatingActionButton(
            icon = { Icon(Icons.Default.Add, "") },
            text = { Text(text = "Add build", Modifier.testTag("add_build")) },
            onClick = { isDirChooserOpen = true },
        )
    }
}