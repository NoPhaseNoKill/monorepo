package com.nophasenokill.legacy.base.surface

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nophasenokill.domain.Content

@Composable
fun PanelSurface(
    modifier: Modifier = Modifier,
    content: Content
) {
    GeneralSurface(type = PanelSurfaceType, modifier, content)
}


