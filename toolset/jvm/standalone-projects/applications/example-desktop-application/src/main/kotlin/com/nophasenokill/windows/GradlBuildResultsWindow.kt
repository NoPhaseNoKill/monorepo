package com.nophasenokill.windows

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import com.nophasenokill.components.CodeBlock
import com.nophasenokill.components.ScrollableColumn

@Composable
fun DisplayResults(result: String, iterations: Int, loading: Boolean) {

    ScrollableColumn(Modifier.fillMaxSize(1f)) {

        val codeBlockText = buildAnnotatedString {
            append("Has content been loaded: ${!loading}. Number of iterations: $iterations \n")
            append("Warmed test run output: \nPlease scroll down to see all output \n")
            append(result)
        }
        CodeBlock(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            code = codeBlockText
        )
    }
}
