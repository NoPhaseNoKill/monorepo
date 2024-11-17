package com.nophasenokill.windows.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nophasenokill.theme.AppTheme

@Composable
fun StyleScreen() {
    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(AppTheme.sizes.medium)
    ) {
        Text(
            text = "Colors",
            color = AppTheme.colors.text,
            style = AppTheme.typography.headline2
        )

        Spacer(modifier = Modifier.height(AppTheme.sizes.medium))

        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium)
        ) {
            ColorSquare(color = AppTheme.colors.primary, "Primary")
            ColorSquare(color = AppTheme.colors.background, "Background")
            ColorSquare(color = AppTheme.colors.basic, "Basic")
            ColorSquare(color = AppTheme.colors.disable, "Disable")
        }

        Spacer(modifier = Modifier.height(AppTheme.sizes.medium))

        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium)
        ) {
            ColorSquare(color = AppTheme.colors.text, "Text")
            ColorSquare(color = AppTheme.colors.textReverse, "TextReverse")
            ColorSquare(color = AppTheme.colors.materialColors.primaryVariant, "TextDisabled")
        }

        Spacer(modifier = Modifier.height(AppTheme.sizes.medium))

        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium)
        ) {
            ColorSquare(color = AppTheme.colors.success, "Success")
            ColorSquare(color = AppTheme.colors.link, "Link")
            ColorSquare(color = AppTheme.colors.warning, "Warning")
            ColorSquare(color = AppTheme.colors.error, "Error")
        }

        Spacer(modifier = Modifier.height(AppTheme.sizes.large))

        Text(
            text = "Sizes",
            color = AppTheme.colors.text,
            style = AppTheme.typography.headline2
        )

        Spacer(modifier = Modifier.height(AppTheme.sizes.medium))

        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.smaller)
        ) {
            SizeLine(size = AppTheme.sizes.smaller, "Smaller")
            SizeLine(size = AppTheme.sizes.small, "Small")
            SizeLine(size = AppTheme.sizes.medium, "Medium")
            SizeLine(size = AppTheme.sizes.large, "Large")
            SizeLine(size = AppTheme.sizes.larger, "Larger")
        }

        Spacer(modifier = Modifier.height(AppTheme.sizes.large))

        Text(
            text = "Typography",
            color = AppTheme.colors.text,
            style = AppTheme.typography.headline2
        )

        Spacer(modifier = Modifier.height(AppTheme.sizes.medium))

        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.small)
        ) {
            TypographyText(AppTheme.typography.headline1, "Headline1")
            TypographyText(AppTheme.typography.headline2, "Headline2")
            TypographyText(AppTheme.typography.headline3, "Headline3")
            TypographyText(AppTheme.typography.headline4, "Headline4")
            TypographyText(AppTheme.typography.headline5, "Headline5")
            TypographyText(AppTheme.typography.headline6, "Headline6")
            TypographyText(AppTheme.typography.subtitle, "Subtitle")
            TypographyText(AppTheme.typography.paragraph, "Paragraph")
            TypographyText(AppTheme.typography.caption, "Caption")
            TypographyText(AppTheme.typography.label, "Label")
            TypographyText(AppTheme.typography.button, "Button")
        }
    }
}

@Composable
private fun ColorSquare(
    color: Color,
    name: String
) {
    val borderColor = if (AppTheme.colors.background == color) AppTheme.colors.text else color
    Column {
        Box(
            modifier = Modifier
                .size(AppTheme.sizes.larger)
                .clip(RoundedCornerShape(AppTheme.sizes.smaller))
                .border(1.dp, borderColor, shape = RoundedCornerShape(AppTheme.sizes.smaller))
                .background(color)
        )
        Text(
            text = name,
            color = AppTheme.colors.text,
            style = AppTheme.typography.label
        )
    }
}

@Composable
private fun SizeLine(
    size: Dp,
    name: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            modifier = Modifier.widthIn(min = 80.dp),
            color = AppTheme.colors.text,
            style = AppTheme.typography.subtitle
        )

        Box(
            modifier = Modifier
                .width(size)
                .height(AppTheme.sizes.smaller)
                .background(AppTheme.colors.primary)
        )
    }
}

@Composable
private fun TypographyText(
    typography: TextStyle,
    name: String
) {
    Text(
        text = name,
        color = AppTheme.colors.primary,
        style = typography
    )
}
