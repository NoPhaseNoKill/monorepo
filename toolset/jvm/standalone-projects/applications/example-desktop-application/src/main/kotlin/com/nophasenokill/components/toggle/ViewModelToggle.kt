package com.nophasenokill.components.toggle

import androidx.compose.material3.Text
import androidx.compose.runtime.*

sealed class View
data object LoadingView: View()
data object TaskSelectionView: View()
data object PlaygroundView: View()
data object LandingPageView: View()
data object ResultPageView: View()

data class ScreenState(private var _currentView: View) {
    
    val currentView: View
        get() = _currentView
    
    fun setView(updated: View) {
        _currentView = updated
    }
}

@Composable
fun ViewModelToggle(state: ScreenState) {
    val currentView by remember { mutableStateOf(state.currentView) }
    
    when (currentView) {
        is LoadingView -> LoadingScreen()
        is TaskSelectionView -> TaskSelectionScreen()
        is PlaygroundView -> PlaygroundScreen()
        is LandingPageView -> LandingPageScreen()
        is ResultPageView -> ResultPageScreen()
    }
}

@Composable
fun LoadingScreen() {
    Text("Loading...")
}

@Composable
fun TaskSelectionScreen() {
    Text("Select a Task")
}

@Composable
fun PlaygroundScreen() {
    Text("Playground")
}

@Composable
fun LandingPageScreen() {
    Text("Welcome to the Landing Page")
}

@Composable
fun ResultPageScreen() {
    Text("Results")
}
