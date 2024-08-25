package com.nophasenokill.components.designsystem.compose

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.window.application
import com.nophasenokill.components.designsystem.compose.ui.DlsTheme
import com.nophasenokill.components.designsystem.compose.ui.dlsDarkColorPalette
import com.nophasenokill.components.designsystem.compose.ui.dlsLightColorPalette

@Composable
fun NewTheme() {
    val isDarkState = rememberSaveable { mutableStateOf(false) }

    DlsTheme(
        colors = if (isDarkState.value) dlsDarkColorPalette() else dlsLightColorPalette()
    ) {
        val (currentSection, setCurrentSection) = rememberSaveable {
            mutableStateOf(HomeSections.Style)
        }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Design System Jetpack Compose",
                            color = DlsTheme.colors.textReverse
                        )
                    },
                    backgroundColor = DlsTheme.colors.primary,
                    actions = {
                        Switch(
                            checked = isDarkState.value,
                            onCheckedChange = { checked ->
                                isDarkState.value = checked
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = DlsTheme.colors.text,
                                uncheckedThumbColor = DlsTheme.colors.basic
                            )
                        )
                    }
                )
            },
            bottomBar = {
                BottomAppBar(
                    backgroundColor = DlsTheme.colors.primary
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
            backgroundColor = DlsTheme.colors.background
        ) { paddingValues ->
            Crossfade(
                targetState = currentSection,
                modifier = Modifier.padding(paddingValues)
            ) { section ->
                when (section) {
                    HomeSections.Style -> StyleScreen()
                    HomeSections.Component -> ComponentScreen()
                }
            }
        }
    }
}

private enum class HomeSections(
    val label: String,
    val icon: ImageVector
) {
    Style("Style", Icons.Outlined.Favorite),
    Component("Component", Icons.Outlined.List),
}
