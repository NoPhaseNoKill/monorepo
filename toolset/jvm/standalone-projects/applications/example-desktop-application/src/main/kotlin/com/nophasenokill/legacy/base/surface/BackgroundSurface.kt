package com.nophasenokill.legacy.base.surface

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nophasenokill.domain.Content

@Composable
fun BackgroundSurface(
    modifier: Modifier = Modifier,
    content: Content
) {
    GeneralSurface(type = BackgroundSurfaceType, modifier, content)
}
