package com.nophasenokill

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun AppLayout(heading: String, content: String, modifier: Modifier = Modifier) {

    Row (
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Top,
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = heading,
                fontSize = Constants.DEFAULT_FONT_SIZE,
                lineHeight = Constants.DEFAULT_LINE_HEIGHT,
                textAlign = TextAlign.Center
            )
            Text(
                text = content,
                fontSize = Constants.DEFAULT_FONT_SIZE,
                lineHeight = Constants.DEFAULT_LINE_HEIGHT,
                textAlign = TextAlign.Center,
            )
            Text(
                text = content,
                fontSize = Constants.DEFAULT_FONT_SIZE,
                lineHeight = Constants.DEFAULT_LINE_HEIGHT,
                textAlign = TextAlign.Center,
            )
        }
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = heading,
                fontSize = Constants.DEFAULT_FONT_SIZE,
                lineHeight = Constants.DEFAULT_LINE_HEIGHT,
                textAlign = TextAlign.Center
            )
            Text(
                text = content,
                fontSize = Constants.DEFAULT_FONT_SIZE,
                lineHeight = Constants.DEFAULT_LINE_HEIGHT,
                textAlign = TextAlign.Center,
            )
        }
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = heading,
                fontSize = Constants.DEFAULT_FONT_SIZE,
                lineHeight = Constants.DEFAULT_LINE_HEIGHT,
                textAlign = TextAlign.Center
            )
            Text(
                text = content,
                fontSize = Constants.DEFAULT_FONT_SIZE,
                lineHeight = Constants.DEFAULT_LINE_HEIGHT,
                textAlign = TextAlign.Center,
            )
            Text(
                text = content,
                fontSize = Constants.DEFAULT_FONT_SIZE,
                lineHeight = Constants.DEFAULT_LINE_HEIGHT,
                textAlign = TextAlign.Center,
            )
            Text(
                text = content,
                fontSize = Constants.DEFAULT_FONT_SIZE,
                lineHeight = Constants.DEFAULT_LINE_HEIGHT,
                textAlign = TextAlign.Center,
            )
        }
    }
    Row (
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = modifier,
        ) {
            Text(
                text = heading,
                fontSize = Constants.DEFAULT_FONT_SIZE,
                lineHeight = Constants.DEFAULT_LINE_HEIGHT,
                textAlign = TextAlign.Center
            )
            Text(
                text = content,
                fontSize = Constants.DEFAULT_FONT_SIZE,
                lineHeight = Constants.DEFAULT_LINE_HEIGHT,
                textAlign = TextAlign.Center,
            )
            Text(
                text = content,
                fontSize = Constants.DEFAULT_FONT_SIZE,
                lineHeight = Constants.DEFAULT_LINE_HEIGHT,
                textAlign = TextAlign.Center,
            )
        }
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = heading,
                fontSize = Constants.DEFAULT_FONT_SIZE,
                lineHeight = Constants.DEFAULT_LINE_HEIGHT,
                textAlign = TextAlign.Center
            )
            Text(
                text = content,
                fontSize = Constants.DEFAULT_FONT_SIZE,
                lineHeight = Constants.DEFAULT_LINE_HEIGHT,
                textAlign = TextAlign.Center,
            )
        }
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = heading,
                fontSize = Constants.DEFAULT_FONT_SIZE,
                lineHeight = Constants.DEFAULT_LINE_HEIGHT,
                textAlign = TextAlign.Center
            )
            Text(
                text = content,
                fontSize = Constants.DEFAULT_FONT_SIZE,
                lineHeight = Constants.DEFAULT_LINE_HEIGHT,
                textAlign = TextAlign.Center,
            )
            Text(
                text = content,
                fontSize = Constants.DEFAULT_FONT_SIZE,
                lineHeight = Constants.DEFAULT_LINE_HEIGHT,
                textAlign = TextAlign.Center,
            )
            Text(
                text = content,
                fontSize = Constants.DEFAULT_FONT_SIZE,
                lineHeight = Constants.DEFAULT_LINE_HEIGHT,
                textAlign = TextAlign.Center,
            )
        }
    }
}