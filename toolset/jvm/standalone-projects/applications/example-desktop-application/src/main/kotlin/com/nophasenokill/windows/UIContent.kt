package com.nophasenokill.windows

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.nophasenokill.domain.CoroutineScopeName
import com.nophasenokill.domain.GradleConnectorName
import com.nophasenokill.theme.AppTheme
import com.nophasenokill.windows.codeviewer.MainDesktopView
import com.nophasenokill.windows.screens.ComponentScreen
import com.nophasenokill.windows.screens.GradleApiDemo
import com.nophasenokill.windows.screens.LazyVerticalGridDemo
import com.nophasenokill.windows.screens.StyleScreen
import kotlinx.coroutines.CoroutineScope
import org.gradle.tooling.ProjectConnection

enum class HomeSections(val icon: ImageVector) {
    Style(Icons.Outlined.Favorite),
    Component(Icons.AutoMirrored.Outlined.List),
    Grid(Icons.Outlined.Grid3x3),
    CodeViewer(Icons.Outlined.Code),
    GradleTask(Icons.Outlined.AttachFile);

    val label: String
        get() = this.name.lowercase().replaceFirstChar { it.uppercaseChar() }
}

@Composable
fun UIContent(
    appScopes: Map<CoroutineScopeName, CoroutineScope>,
    connectors: Map<GradleConnectorName, ProjectConnection>,
) {


    AppTheme { _, section ->
        when (section) {
            HomeSections.Style -> StyleScreen()
            HomeSections.Component -> ComponentScreen()
            HomeSections.Grid -> LazyVerticalGridDemo()
            HomeSections.GradleTask -> GradleApiDemo(appScopes, connectors)
            HomeSections.CodeViewer -> MainDesktopView()
        }
    }
}
