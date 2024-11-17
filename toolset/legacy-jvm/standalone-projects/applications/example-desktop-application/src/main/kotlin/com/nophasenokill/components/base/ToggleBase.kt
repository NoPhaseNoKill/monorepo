package com.nophasenokill.components.base

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun ToggleBase(
    onChange:(value: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var on by remember { mutableStateOf(true) }

    fun onToggleChange(old: Boolean, new: Boolean) {
        println("Changing theme toggle value from: ${old} to: ${new}")
        onChange(new)
    }

    Switch(
        modifier = modifier,
        checked = on,
        onCheckedChange = {
            onToggleChange(on, it)

            on = it
        },
        thumbContent = if (on) {
            {

                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )
            }
        } else {
            null
        }
    )

}
