package com.nophasenokill.windows

import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.nophasenokill.windows.platform.HomeFolder
import com.nophasenokill.windows.ui.CodeViewer
import com.nophasenokill.windows.ui.CodeViewerView
import com.nophasenokill.windows.ui.common.LocalTheme
import com.nophasenokill.windows.ui.common.Settings
import com.nophasenokill.windows.ui.common.Theme
import com.nophasenokill.windows.ui.editor.Editors
import com.nophasenokill.windows.ui.filetree.FileTree

@Composable
fun MainUiView() {
    val codeViewer = remember {
        val editors = Editors()

        CodeViewer(
            editors = editors,
            fileTree = FileTree(HomeFolder, editors),
            settings = Settings()
        )
    }

    DisableSelection {
        val theme = Theme.dark

        CompositionLocalProvider(
            LocalTheme provides theme,
        ) {
            MaterialTheme(
                colors = theme.materialColors
            ) {
                Surface {
                    CodeViewerView(codeViewer)
                }
            }
        }
    }
}
