package com.nophasenokill.components.splitter

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import java.awt.Cursor

private fun Modifier.cursorForHorizontalResize(isHorizontal: Boolean): Modifier =
    pointerHoverIcon(PointerIcon(Cursor(if (isHorizontal) Cursor.E_RESIZE_CURSOR else Cursor.S_RESIZE_CURSOR)))

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
private fun DesktopHandle(
    isHorizontal: Boolean,
    splitPaneState: SplitPaneState
) = Box(
    Modifier
        .run {
            val layoutDirection = LocalLayoutDirection.current
            pointerInput(splitPaneState) {
                detectDragGestures { change, _ ->
                    change.consume()
                    splitPaneState.dispatchRawMovement(
                        if (isHorizontal)
                            if (layoutDirection == LayoutDirection.Ltr) change.position.x else -change.position.x
                        else change.position.y
                    )
                }
            }
        }
        .cursorForHorizontalResize(isHorizontal)
        .run {
            if (isHorizontal) {
                this.width(8.dp)
                    .fillMaxHeight()
            } else {
                this.height(8.dp)
                    .fillMaxWidth()
            }
        }
)

@OptIn(ExperimentalSplitPaneApi::class)
internal fun defaultSplitter(
    isHorizontal: Boolean,
    splitPaneState: SplitPaneState
): Splitter = Splitter(
    measuredPart = {},
    handlePart = {
        DesktopHandle(isHorizontal, splitPaneState)
    }
)

