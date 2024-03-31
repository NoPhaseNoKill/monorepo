package com.nophasenokill

import com.nophasenokill.functionalTest.FunctionalTest
import com.nophasenokill.functionalTest.TestLogger
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.Logger.ROOT_LOGGER_NAME
import org.slf4j.LoggerFactory
import kotlin.system.measureTimeMillis
import kotlin.time.Duration.Companion.seconds


class SpeedUpTestInvocationListener: FunctionalTest() {

    @Test
    fun `should finish test almost immediately even though there normally would be delay of 60 seconds`() = runTest {

        val time = measureTimeMillis {
            val deferred: Deferred<String> = async {

                delay(30.seconds)

                async {
                    delay(30.seconds)
                }.await()

                return@async "bob"
            }
            val result = deferred.await() // result available immediately
            Assertions.assertEquals("bob", result)
        }

        TestLogger.LOGGER.error { "Ohai" }
        TestLogger.LOGGER.info { "Ohai as info" }

        Assertions.assertTrue(time <= 6000)
    }
}
