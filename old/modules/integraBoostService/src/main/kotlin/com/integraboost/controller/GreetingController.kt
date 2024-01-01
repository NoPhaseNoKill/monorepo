package com.integraboost.controller

import com.integraboost.domain.Greeting
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

@RestController
class GreetingController {
    private val counter = AtomicLong()

    @GetMapping("/greeting")
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String?): Greeting {
        LOGGER.info("Coming into greeting controller")
        return Greeting(counter.incrementAndGet(), String.format(template, name))
    }

    companion object {
        private const val template = "Hello, %s!"

        @JvmStatic
        val LOGGER: Logger = LoggerFactory.getLogger(this::class.java.declaringClass)
    }
}