package com.nophasenokill.legacy.base.surface

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.nophasenokill.domain.Content

sealed interface GeneralSurfaceType
data object BackgroundSurfaceType: GeneralSurfaceType
data object PanelSurfaceType: GeneralSurfaceType
data object ContentSurfaceType: GeneralSurfaceType

/*
    Serves as the basic building blocks for the page.

    Exposes three types of surfaces:
        1. A background surface -> the surface furthest away from the reader
        2. A panel surface -> the surface in between the reader and content
        3. A content surface -> the surface that displays content

        See AllSurfaces for rendered surfaces example
 */

@Composable
fun GeneralSurface(
    type: GeneralSurfaceType,
    modifier: Modifier = Modifier,
    content: Content
) {
    val color = when(type) {
        BackgroundSurfaceType -> MaterialTheme.colorScheme.background
        PanelSurfaceType -> MaterialTheme.colorScheme.surface
        ContentSurfaceType -> MaterialTheme.colorScheme.onSecondaryContainer
    }
    Box(
        modifier = modifier
            .width(200.dp)
            .height(300.dp)
            .padding(16.dp)
            .shadow(8.dp, MaterialTheme.shapes.medium) // Added shadow for better separation
            .background(color)
            .clip(MaterialTheme.shapes.medium)
    ) {
        content()
    }
}
