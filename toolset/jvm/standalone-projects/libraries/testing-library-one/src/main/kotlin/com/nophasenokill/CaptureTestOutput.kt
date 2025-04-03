package com.nophasenokill

import java.io.ByteArrayOutputStream
import java.io.PrintStream

fun captureTestOutput(block: () -> Unit): String {
    ByteArrayOutputStream().use { stream ->
        PrintStream(stream) .use { printStream ->
            System.setOut(printStream)
            try {
                block()
            } finally {
                System.setOut(System.out)
            }
            return stream.toString().trim()
        }
    }
}
