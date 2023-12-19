package com.integraboost.controller

import com.integraboost.extensions.SharedAppExtension
import com.integraboost.extensions.TestInvocationListener
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@ExtendWith(SharedAppExtension::class, TestInvocationListener::class)
open class IntegrationTest {

    companion object {
        @JvmStatic
        val LOGGER: Logger = LoggerFactory.getLogger(this::class.java.declaringClass)
    }
}