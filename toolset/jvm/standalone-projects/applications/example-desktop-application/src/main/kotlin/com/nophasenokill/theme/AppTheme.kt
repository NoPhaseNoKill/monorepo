package com.nophasenokill.theme

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.Folder
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nophasenokill.components.AddBuildButton
import com.nophasenokill.components.DirChooserDialog
import com.nophasenokill.windows.HomeSections
import javax.swing.JFileChooser

@Composable
fun AppTheme(
    onJavaDirChange: (value: String) -> Unit,
    sectionContent: @Composable (PaddingValues, HomeSections) -> Unit,

) {
    val isDarkState = rememberSaveable { mutableStateOf(false) }
    var javaDirInput by remember { mutableStateOf("") }
    val colors1 = if (isDarkState.value) darkColorPalette() else lightColorPalette()
    val typography1 = AppTypography()



    fun showDirectoryChooser(onDirectorySelected: (String) -> Unit) {
        val chooser = JFileChooser().apply {
            fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        }
        val returnVal = chooser.showOpenDialog(null)
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            onDirectorySelected(chooser.selectedFile.absolutePath)
        }
    }


    CompositionLocalProvider(
        LocalDlsColors provides colors1,
        LocalAppTypography provides typography1,
    ) {
        MaterialTheme(
            colors = colors1.materialColors,
            typography = typography1.materialTypography
        ) {
            val (currentSection, setCurrentSection) = rememberSaveable {
                mutableStateOf(HomeSections.Style)
            }
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "NoPhaseNoKill Gradle App",
                                color = AppTheme.colors.textReverse
                            )
                        },
                        backgroundColor = AppTheme.colors.primary,
                        actions = {
                            Switch(
                                checked = isDarkState.value,
                                onCheckedChange = { checked ->
                                    isDarkState.value = checked
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = AppTheme.colors.text,
                                    uncheckedThumbColor = AppTheme.colors.basic
                                )
                            )
                        }
                    )
                },
                bottomBar = {
                    BottomAppBar(
                        backgroundColor = AppTheme.colors.primary
                    ) {

                        IconButton(
                            onClick = {
                                showDirectoryChooser { selectedDir ->
                                    javaDirInput = selectedDir
                                    onJavaDirChange(selectedDir)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Folder,
                                contentDescription = "Select Directory",
                                tint = AppTheme.colors.text
                            )
                        }

                        val navItems = HomeSections.entries
                        navItems.forEach { section ->
                            BottomNavigationItem(
                                selected = currentSection == section,
                                onClick = { setCurrentSection(section) },
                                icon = { Icon(imageVector = section.icon, contentDescription = null) },
                                label = { Text(section.label) }
                            )
                        }
                    }
                },
                backgroundColor = AppTheme.colors.background,
                content = { paddingValues ->
                    Crossfade(
                        targetState = currentSection,
                        modifier = Modifier.padding(paddingValues)
                    ) { section ->
                        sectionContent(paddingValues, section)
                    }
                }
            )
        }
    }
}


object AppTheme {
    val colors: ColorPalette
        @Composable
        @ReadOnlyComposable
        get() = LocalDlsColors.current

    val typography: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalAppTypography.current

    val sizes: AppSize
        @Composable
        @ReadOnlyComposable
        get() = AppSize()
}

internal val LocalDlsColors = staticCompositionLocalOf { lightColorPalette() }
internal val LocalAppTypography = staticCompositionLocalOf { AppTypography() }

