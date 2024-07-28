package com.nophasenokill;

fun main() {
    val appOutput = TestInstrumentationApp.doSomething()
    println(appOutput)
}

object TestInstrumentationApp {
    fun doSomething(): String {
        val calculatedValue = Calculator.add()
        assert(calculatedValue == 4) {
            "Expected calculator/calculator value to be present and equal to 4 and was not."
        }
        return "App has NOT been instrumented"
    }
}