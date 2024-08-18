package com.nophasenokill.components.layout

import androidx.compose.runtime.*

sealed interface ToggleValueLegacy
data object ToggledOnLegacy: ToggleValueLegacy
data object ToggledOffLegacy: ToggleValueLegacy

@Composable
fun ToggleBackgroundColourButton(
    onClick: () -> Unit,
) {
    println("Rendering ToggleBackgroundColourButton")

    fun onToggleClick() {
        println("TriggerBackgroundColourChange clicked pre-onClick")
        onClick()
        println("TriggerBackgroundColourChange clicked post-onClick")
    }

    TriggerActionButton(TriggerBackgroundColourChange) {
        onToggleClick()
    }
}
