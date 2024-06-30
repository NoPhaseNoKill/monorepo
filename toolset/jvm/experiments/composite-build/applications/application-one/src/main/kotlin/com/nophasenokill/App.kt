package com.nophasenokill;

import org.gradle.api.logging.Logging

object App {
    /**
     * Run the application.
     *
     * @param args command line arguments are ignored
     */
    @JvmStatic
    fun main(args: Array<String>) {
        Service.printMessage(MessageModel("007.26"))
        val result = Calculator.add(2, 3)
        LOGGER.quiet("Using calculator from library-one in application-two. Result should be 2 + 3 = 5 . Result was: $result")
    }

    val LOGGER = Logging.getLogger(this::class.java)
}