package com.nophasenokill.legacy.base.buttons

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.nophasenokill.domain.Content


sealed interface ButtonSurfaceType
data object BackgroundButtonType: ButtonSurfaceType
data object PanelButtonType: ButtonSurfaceType
data object ContentButtonType: ButtonSurfaceType

@Composable
fun BackgroundButton(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    content: Content
) {
    SurfaceButton(
        type = BackgroundButtonType,
        modifier = modifier.fillMaxSize(),
        onClick = onClick
    ) {
        content()
    }
}

@Composable
fun PanelButton(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    content: Content
) {
    SurfaceButton(
        type = PanelButtonType,
        modifier = modifier,
        onClick = onClick
    ) {
        content()
    }
}

@Composable
fun ContentButton(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    content: Content
) {
    SurfaceButton(
        type = PanelButtonType,
        modifier = modifier,
        onClick = onClick
    ) {
        content()
    }
}

@Composable
@Preview
fun ExampleButton() {
    fun onClick() {
        println("You should see this message when you click the button")
    }

    BackgroundButton(onClick = ::onClick) {
        Text("I am a background button on a background surface.")
    }

    PanelButton(onClick = ::onClick) {
        Text("I am a panel button on a background surface.")
    }

    ContentButton(onClick = ::onClick) {
        Text("I am a content button on a background surface.")
    }
}


@Composable
fun SurfaceButton(
    type: ButtonSurfaceType,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    content: Content
) {

    val containerColor = when(type) {
        BackgroundButtonType -> MaterialTheme.colorScheme.inverseSurface
        ContentButtonType -> MaterialTheme.colorScheme.secondary
        PanelButtonType -> MaterialTheme.colorScheme.primary
    }

    val contentColor = when(type) {
        BackgroundButtonType -> MaterialTheme.colorScheme.inverseSurface
        ContentButtonType -> MaterialTheme.colorScheme.onSecondary
        PanelButtonType -> MaterialTheme.colorScheme.onPrimary
    }

    fun onButtonClick() {
        println("The button's on click is about to be called")
        onClick()
        println("The button's on click was called")
    }

    Button(
        onClick = {
            onButtonClick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = modifier
            .padding(8.dp)
            .height(50.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
    ) {
        content()
    }
}
