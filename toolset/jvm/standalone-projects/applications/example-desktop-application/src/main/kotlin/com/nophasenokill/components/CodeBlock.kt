package com.nophasenokill.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// @Composable
// fun CodeBlock(
//     modifier: Modifier = Modifier,
//     code: AnnotatedString,
//     onClick: (Any) -> Unit = {},
// ) {
//
//     println("Rendering CodeBlock")
//
//     Surface(
//         tonalElevation = 8.dp,
//         shadowElevation = 8.dp,
//         color = MaterialTheme.colorScheme.onPrimaryContainer,
//         contentColor = MaterialTheme.colorScheme.onSurface,
//         shape = MaterialTheme.shapes.extraSmall,
//         modifier = modifier,
//
//     ) {
//
//         ClickableText(
//             text = code,
//             modifier = Modifier.padding(12.dp),
//             style = MaterialTheme.typography.labelMedium.copy(fontFamily = FontFamily.Monospace),
//             onClick = onClick
//         )
//
//     }
// }

@Composable
fun CodeBlock(
    modifier: Modifier = Modifier,
    code: AnnotatedString,
    onClick: (Int) -> Unit = {},
    onButtonClick: () -> Unit = {}
) {

    Surface(
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
        color =  MaterialTheme.colorScheme.onPrimaryContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = MaterialTheme.shapes.extraSmall,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            ClickableText(
                text = code,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelMedium.copy(fontFamily = FontFamily.Monospace),
                onClick = { int ->
                    onClick(int)
                    onButtonClick()
                }
            )

            // Spacer(modifier = Modifier.height(8.dp)) // Space between text and button
        }
    }
}

@Composable
fun ClickableText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    softWrap: Boolean = true,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    onClick: (Int) -> Unit
) {
    var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    BasicText(
        text = text,
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures { pos ->
                layoutResult?.let {
                    val offset = it.getOffsetForPosition(pos)
                    onClick(offset)
                }
            }
        },
        style = style.copy(
            lineHeight = 20.sp,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Proportional,
                trim = LineHeightStyle.Trim.None
            ),
            letterSpacing = (-0.5).sp,
        ),
        softWrap = softWrap,
        overflow = overflow,
        maxLines = maxLines,
        onTextLayout = { textLayoutResult: TextLayoutResult ->
            layoutResult = textLayoutResult
            onTextLayout(textLayoutResult)
        }
    )
}
