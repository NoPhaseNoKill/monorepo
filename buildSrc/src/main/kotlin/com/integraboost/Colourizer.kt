package com.integraboost

object Colourizer {
    fun colourize(text: String, colour: AnsiColour): String {
        return "${colour}$text${AnsiColour.NONE}"
    }
}