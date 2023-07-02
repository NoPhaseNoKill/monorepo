package com.integraboost

import org.gradle.api.tasks.testing.TestResult

enum class AnsiColour(private val ansiCode: Int) {
    NONE(0),
    BLACK(30),
    RED(31),
    GREEN(32),
    YELLOW(33),
    BLUE(34),
    PURPLE(35),
    CYAN(36),
    WHITE(37);

    val ansiString: String = "\u001B[${ansiCode}m"

    override fun toString(): String {
        return ansiString
    }
}