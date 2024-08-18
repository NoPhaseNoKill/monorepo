package com.nophasenokill.components.layout

import androidx.compose.runtime.Composable

sealed class TriggerAction(val text: String)
data object TriggerBackgroundColourChange: TriggerAction("Update background colour")
data object TriggerPrintAction: TriggerAction("Triggers println for the action")

@Composable
fun TriggerActionButton(action: TriggerAction, onClick: () -> Unit) {
    println("Rendering TriggerActionButton")
    TextButton(action.text, onClick)
}
