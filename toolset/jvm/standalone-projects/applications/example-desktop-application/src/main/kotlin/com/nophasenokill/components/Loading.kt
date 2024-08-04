package com.nophasenokill.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nophasenokill.domain.Constants

@Composable
fun Loading() {
    Box(
        modifier = Modifier.fillMaxSize().padding(top = Constants.DEFAULT_PADDING),
        contentAlignment = Alignment.Center
    ) {
        Column {
            CircularProgressIndicator()
        }
    }
}
