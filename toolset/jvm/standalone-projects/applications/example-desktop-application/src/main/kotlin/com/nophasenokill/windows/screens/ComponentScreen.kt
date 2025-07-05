package com.nophasenokill.windows.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nophasenokill.components.designsystem.Avatar
import com.nophasenokill.components.designsystem.ListItem
import com.nophasenokill.components.designsystem.PrimaryButton
import com.nophasenokill.components.designsystem.SecondaryButton
import com.nophasenokill.theme.AppTheme

@Composable
fun ComponentScreen() {
    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(AppTheme.sizes.medium)
    ) {
        Text(
            text = "Button",
            color = AppTheme.colors.text,
            style = AppTheme.typography.headline2
        )

        Spacer(modifier = Modifier.height(AppTheme.sizes.medium))

        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium)
        ) {
            PrimaryButton("PrimaryButton") { }
            SecondaryButton("SecondaryButton") { }
        }

        Spacer(modifier = Modifier.height(AppTheme.sizes.medium))

        Text(
            text = "Avatar",
            color = AppTheme.colors.text,
            style = AppTheme.typography.headline2
        )

        Spacer(modifier = Modifier.height(AppTheme.sizes.medium))

        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium),
            verticalAlignment = Alignment.Bottom
        ) {
            val catPainter = loadAvatarPainter("drawable/cat.png")
            Avatar(
                // useResource("ic_launcher.png", ::loadImageBitmap)
                painter = catPainter,
                sizeVariant = Avatar.SizeVariant.Large
            )
            Avatar(
                painter = catPainter,
                sizeVariant = Avatar.SizeVariant.Medium
            )
            Avatar(
                painter = catPainter,
                sizeVariant = Avatar.SizeVariant.Small
            )
        }

        Spacer(modifier = Modifier.height(AppTheme.sizes.medium))

        Text(
            text = "List Item",
            color = AppTheme.colors.text,
            style = AppTheme.typography.headline2
        )

        Spacer(modifier = Modifier.height(AppTheme.sizes.medium))

        Surface(
            elevation = AppTheme.sizes.smaller
        ) {
            ListItem(title = "Title") {
                Text(
                    text = "Body",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .border(1.dp, color = AppTheme.colors.success),
                    color = AppTheme.colors.success,
                    textAlign = TextAlign.Center,
                    style = AppTheme.typography.headline3
                )
            }
        }
    }
}

fun loadAvatarPainter(resourceName: String): Painter {

    return BitmapPainter(useResource(resourceName, ::loadImageBitmap))

    // val classLoader = Thread.currentThread().contextClassLoader
    // val inputStream: InputStream = classLoader.getResourceAsStream(resourceName)
    //     ?: throw IllegalArgumentException("Resource not found: $resourceName")
    //
    // val image = ImageBitmap(::loadImageBitmap)
    // return BitmapPainter(image.toImage().toComposeImageBitmap())
}
