package com.nophasenokill.theme

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.nophasenokill.windows.HomeSections

@Composable
fun AppTheme(
    sectionContent: @Composable (PaddingValues, HomeSections) -> Unit
) {
    val isDarkState = rememberSaveable { mutableStateOf(false) }

    val colors1 = if (isDarkState.value) darkColorPalette() else lightColorPalette()
    val typography1 = AppTypography()
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

