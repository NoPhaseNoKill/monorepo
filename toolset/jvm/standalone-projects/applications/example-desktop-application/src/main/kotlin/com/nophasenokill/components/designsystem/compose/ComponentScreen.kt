package com.nophasenokill.components.designsystem.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.nophasenokill.components.designsystem.compose.component.Avatar
import com.nophasenokill.components.designsystem.compose.component.ListItem
import com.nophasenokill.components.designsystem.compose.component.PrimaryButton
import com.nophasenokill.components.designsystem.compose.component.SecondaryButton
import com.nophasenokill.components.designsystem.compose.ui.DlsTheme

@Composable
fun ComponentScreen() {
    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(DlsTheme.sizes.medium)
    ) {
        Text(
            text = "Button",
            color = DlsTheme.colors.text,
            style = DlsTheme.typography.headline2
        )

        Spacer(modifier = Modifier.height(DlsTheme.sizes.medium))

        Row(
            horizontalArrangement = Arrangement.spacedBy(DlsTheme.sizes.medium)
        ) {
            PrimaryButton("PrimaryButton") { }
            SecondaryButton("SecondaryButton") { }
        }

        Spacer(modifier = Modifier.height(DlsTheme.sizes.medium))

        Text(
            text = "Avatar",
            color = DlsTheme.colors.text,
            style = DlsTheme.typography.headline2
        )

        Spacer(modifier = Modifier.height(DlsTheme.sizes.medium))

        Row(
            horizontalArrangement = Arrangement.spacedBy(DlsTheme.sizes.medium),
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

        Spacer(modifier = Modifier.height(DlsTheme.sizes.medium))

        Text(
            text = "List Item",
            color = DlsTheme.colors.text,
            style = DlsTheme.typography.headline2
        )

        Spacer(modifier = Modifier.height(DlsTheme.sizes.medium))

        Surface(
            elevation = DlsTheme.sizes.smaller
        ) {
            ListItem(title = "Title") {
                Text(
                    text = "Body",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .border(1.dp, color = DlsTheme.colors.success),
                    color = DlsTheme.colors.success,
                    textAlign = TextAlign.Center,
                    style = DlsTheme.typography.headline3
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
