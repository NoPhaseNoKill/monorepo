package com.nophasenokill.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nophasenokill.components.base.ToggleBase

@Composable
fun ThemeToggle(
    modifier: Modifier = Modifier
) {
    ToggleBase(onChange = { value ->
        println("Using the value: ${value} from ToggleBase inside of ThemeToggle")
    },modifier)
}
