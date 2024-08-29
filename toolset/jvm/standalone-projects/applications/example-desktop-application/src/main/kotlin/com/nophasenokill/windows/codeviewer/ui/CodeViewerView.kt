package com.nophasenokill.windows.ui

import androidx.compose.animation.core.Spring.StiffnessLow
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.nophasenokill.windows.ui.editor.EditorEmptyView
import com.nophasenokill.windows.ui.editor.EditorTabsView
import com.nophasenokill.windows.ui.editor.EditorView
import com.nophasenokill.windows.ui.filetree.FileTreeView
import com.nophasenokill.windows.ui.filetree.FileTreeViewTabView
import com.nophasenokill.windows.ui.statusbar.StatusBar
import com.nophasenokill.windows.util.SplitterState
import com.nophasenokill.windows.util.VerticalSplittable

@Composable
fun CodeViewerView(model: CodeViewer) {
    val panelState = remember { PanelState() }

    val animatedSize = if (panelState.splitter.isResizing) {
        if (panelState.isExpanded) panelState.expandedSize else panelState.collapsedSize
    } else {
        animateDpAsState(
            if (panelState.isExpanded) panelState.expandedSize else panelState.collapsedSize,
            SpringSpec(stiffness = StiffnessLow)
        ).value
    }

    Box(Modifier
        .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        VerticalSplittable(
            Modifier.fillMaxSize(),
            panelState.splitter,
            onResize = {
                panelState.expandedSize =
                    (panelState.expandedSize + it).coerceAtLeast(panelState.expandedSizeMin)
            }
        ) {
            ResizablePanel(Modifier.width(animatedSize).fillMaxHeight(), panelState) {
                Column {
                    FileTreeViewTabView()
                    FileTreeView(model.fileTree)
                }
            }

            Box {
                if (model.editors.active != null) {
                    Column(Modifier.fillMaxSize()) {
                        EditorTabsView(model.editors)
                        Box(Modifier.weight(1f)) {
                            EditorView(model.editors.active!!, model.settings)
                        }
                        StatusBar(model.settings)
                    }
                } else {
                    EditorEmptyView()
                }
            }
        }
    }
}

private class PanelState {
    val collapsedSize = 24.dp
    var expandedSize by mutableStateOf(300.dp)
    val expandedSizeMin = 90.dp
    var isExpanded by mutableStateOf(true)
    val splitter = SplitterState()
}

@Composable
private fun ResizablePanel(
    modifier: Modifier,
    state: PanelState,
    content: @Composable () -> Unit,
) {
    val alpha by animateFloatAsState(if (state.isExpanded) 1f else 0f, SpringSpec(stiffness = StiffnessLow))

    Box(modifier) {
        Box(Modifier.fillMaxSize().graphicsLayer(alpha = alpha)) {
            content()
        }

        Icon(
            if (state.isExpanded) Icons.AutoMirrored.Filled.ArrowBack else Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = if (state.isExpanded) "Collapse" else "Expand",
            tint = LocalContentColor.current,
            modifier = Modifier
                .padding(top = 4.dp)
                .width(24.dp)
                .clickable {
                    state.isExpanded = !state.isExpanded
                }
                .padding(4.dp)
                .align(Alignment.TopEnd)
        )
    }
}
