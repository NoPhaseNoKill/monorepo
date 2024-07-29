package com.nophasenokill.client.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nophasenokill.client.ui.theme.spacing

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBar(
    onBackClick: (() -> Unit)? = null,
    title: @Composable () -> Unit,
) {
    TopAppBar(
        modifier = Modifier.padding(MaterialTheme.spacing.level0)
            .height(MaterialTheme.spacing.topBarHeight)
            .fillMaxWidth(),
        navigationIcon = {
            if (onBackClick != null) {
                Box(Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                    BackIcon(onBackClick)
                }
            }
        },
        title = {
            Row(Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
                title()
            }
        }
    )
}
