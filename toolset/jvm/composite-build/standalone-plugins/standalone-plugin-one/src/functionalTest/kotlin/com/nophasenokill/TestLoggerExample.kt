package com.nophasenokill

import com.nophasenokill.functionalTest.FunctionalTest
import com.nophasenokill.functionalTest.TestLogger
import org.junit.jupiter.api.Test


class TestLoggerExample: FunctionalTest() {

    @Test
    fun test1() {
        TestLogger.LOGGER.info { "message from test 1"}
    }

    @Test
    fun test2() {
        TestLogger.LOGGER.info { "message from test 2" }
    }
}
