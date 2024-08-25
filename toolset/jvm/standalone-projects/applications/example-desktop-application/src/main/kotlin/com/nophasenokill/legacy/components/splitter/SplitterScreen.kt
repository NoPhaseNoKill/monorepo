package com.nophasenokill.components.splitter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nophasenokill.theme.ColorsSecondaryBackground
import com.nophasenokill.theme.SoftPink
import org.gradle.tooling.ProjectConnection
import java.awt.Cursor

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
fun SplitterScreen(
    gradleConnector: ProjectConnection,
    task: String,
    contentSectionOne: @Composable () -> Unit,
    contentSectionTwo: @Composable () -> Unit,
    contentSectionThree: @Composable () -> Unit,
) {

    val splitterState = rememberSplitPaneState()
    val hSplitterState = rememberSplitPaneState()
    var delta by remember { mutableStateOf("20") }
    var percentage by remember { mutableStateOf("0.20") }

    fun Modifier.cursorForHorizontalResize(): Modifier =
        pointerHoverIcon(PointerIcon(Cursor(Cursor.E_RESIZE_CURSOR)))

    Row {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(10.dp).width(180.dp)
        ) {
            Text("Action panel", fontWeight = FontWeight.Bold)
            Button(onClick = { splitterState.moveEnabled = !splitterState.moveEnabled }) {
                Text(if (splitterState.moveEnabled) "Freeze V" else "Unfreeze V")
            }
            Button(onClick = { hSplitterState.moveEnabled = !hSplitterState.moveEnabled }) {
                Text(if (hSplitterState.moveEnabled) "Freeze H" else "Unfreeze H")
            }

            OutlinedTextField(value = delta, onValueChange = { delta = it }, label = { Text("Delta") })
            Button(onClick = { delta.toFloatOrNull()?.let { splitterState.dispatchRawMovement(it) } }) {
                Text("Add delta V")
            }
            Button(onClick = { delta.toFloatOrNull()?.let { hSplitterState.dispatchRawMovement(it) } }) {
                Text("Add delta H")
            }

            OutlinedTextField(
                value = percentage,
                onValueChange = { percentage = it },
                label = { Text("Fraction") })
            Button(onClick = { percentage.toFloatOrNull()?.let { splitterState.positionPercentage = it } }) {
                Text("Set fraction V")
            }
            Button(onClick = { percentage.toFloatOrNull()?.let { hSplitterState.positionPercentage = it } }) {
                Text("Set fraction H")
            }
        }
        HorizontalSplitPane(
            splitPaneState = splitterState
        ) {
            first(200.dp) {
                Box(Modifier.background(Color.Red).fillMaxSize()) {
                    contentSectionOne()
                }

                // Box(Modifier.background(Color.Red).fillMaxSize())
            }
            second(70.dp) {
                VerticalSplitPane(splitPaneState = hSplitterState) {

                    first(475.dp) {
                        // Box(Modifier.background(Color.Blue).fillMaxSize())
                        // Box(Modifier.background(Color.Blue).fillMaxSize()) {
                        //     Column {
                        //         AppLayout(modifier = Modifier.padding(Constants.DEFAULT_PADDING), gradleConnector, task)
                        //     }
                        // }
                        Box(Modifier.background(ColorsSecondaryBackground.color).fillMaxSize()) {

                            contentSectionTwo()
                            // Surface(Modifier.background(Color.Transparent)) {
                            //
                            // }

                        }

                    }
                    second(20.dp) {

                        Box(Modifier.background(SoftPink).fillMaxSize()) {
                            contentSectionThree()
                        }

                        // Surface(Modifier.background(Color.Transparent)) {
                        //     // DirChooserDialog("Pick a file!", false, {})
                        // }
                    }
                }
            }
            splitter {
                visiblePart {
                    Box(
                        Modifier
                            .width(1.dp)
                            .fillMaxHeight()
                            .background(MaterialTheme.colors.background)
                    )
                }
                handle {
                    Box(
                        Modifier
                            .markAsHandle()
                            .cursorForHorizontalResize()
                            .background(SolidColor(Color.Gray), alpha = 0.50f)
                            .width(9.dp)
                            .fillMaxHeight()
                    )
                }
            }
        }
    }
}
