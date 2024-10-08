package com.nophasenokill.components.designsystem

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import com.nophasenokill.theme.AppTheme

object Avatar {
    sealed class SizeVariant {
        @get:Composable
        abstract val value: Dp

        object Small : SizeVariant() {
            override val value: Dp
                @Composable
                get() = AppTheme.sizes.medium
        }

        object Medium : SizeVariant() {
            override val value: Dp
                @Composable
                get() = AppTheme.sizes.large
        }

        object Large : SizeVariant() {
            override val value: Dp
                @Composable
                get() = AppTheme.sizes.larger
        }
    }
}

@Composable
fun Avatar(
    painter: Painter,
    sizeVariant: Avatar.SizeVariant
) {
    Box(
        modifier = Modifier
            .size(sizeVariant.value)
            .clip(CircleShape)
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
