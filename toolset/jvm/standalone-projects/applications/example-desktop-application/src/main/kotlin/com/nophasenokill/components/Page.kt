package com.nophasenokill.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nophasenokill.domain.Constants

@Composable
fun Page(content: @Composable () -> Unit) {
    /*
        A box wraps a surface, of which the surface fills the max size (aka everything)

        TODO actually figure out if this is correct or not? Looks like screen tears
     */
    Box(modifier = Modifier.fillMaxHeight(1f).fillMaxWidth(1f)) {
        Surface(
            tonalElevation = Constants.DEFAULT_ELEVATION,
            shadowElevation =Constants. DEFAULT_ELEVATION,
            modifier = Modifier.fillMaxSize(),
        ) {
            content()
        }
    }

}
