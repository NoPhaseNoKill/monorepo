package com.nophasenokill.client.ui

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.nophasenokill.client.ui.build.BuildContent
import com.nophasenokill.client.ui.buildlist.BuildListContent
import com.nophasenokill.client.ui.connected.ConnectedContent
import com.nophasenokill.client.ui.theme.GradleClientTheme

@Composable
fun UiContent(uiComponent: UiComponent) {
    GradleClientTheme {
        Children(uiComponent.childStack) {
            when (val child = it.instance) {
                is UiComponent.Child.BuildList -> BuildListContent(child.component)
                is UiComponent.Child.Build -> BuildContent(child.component)
                is UiComponent.Child.Connected -> ConnectedContent(child.component)
            }
        }
    }
}
