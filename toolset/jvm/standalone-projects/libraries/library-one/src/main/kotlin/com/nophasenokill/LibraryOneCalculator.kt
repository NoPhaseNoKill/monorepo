package com.nophasenokill

object LibraryOneCalculator {
    fun add(a: Int, b: Int): Int {
        multiply(a, b)
        return a + b
    }

    fun subtract(a: Int, b: Int): Int {
        // println("Subtracting...")
        return a - b
    }

    /*
        Uncomment this to showcase how you might avoid running tests for something unrelated to tests
        that have previously passed. We'll call this: 'The holy grail of incremental feedback'.
     */

    fun multiply(a: Int, b: Int): Int {
        return a * b
    }
}
