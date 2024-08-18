package com.nophasenokill.components.layout

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import com.nophasenokill.components.CodeBlock
import com.nophasenokill.domain.Constants


@Composable
fun LazyGrid(
    columns: GridCells.Adaptive,
    section: String,
    content: @Composable () -> Unit
) {

    LazyVerticalGrid(
        columns = columns,
        modifier = Modifier
            .fillMaxSize()
            .padding(Constants.DEFAULT_PADDING),
    ) {
        item {
            Text(text = section)
            content()
        }
    }

}


@Composable
fun HomeScreenGrid() {

    println("Rendering HomeScreenGrid")
    val sections = listOf("First", "Second", "Third")
    // SectionRow(
    //     Modifier
    //         .fillMaxSize()
    //         .padding(Constants.DEFAULT_PADDING)
    // ) {
        // SectionColumn {
        //     AppTheme(onThemeChange) {
                sections.forEach { section ->
                    // LazyGrid(columns = GridCells.Adaptive(minSize = 128.dp), section) {

                        SectionRow {
                            SectionColumn {
                                if (section == "First") {
                                    // ToggleBackgroundColourButton(themeValue = themeValue) { value ->
                                    //     onThemeChange(value)
                                    // }

                                } else {
                                    CodeBlock(
                                        modifier = Modifier,
                                        code = buildAnnotatedString { append("Ohai") },
                                        onClick = { println("You clicked the code block") }
                                    )
                                }
                            }
                        }
                    // }

                // }
            // }
        // }
    }
}
