package com.integraboost.controller

import com.integraboost.IntegrationTest
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class GreetingControllerTests: IntegrationTest() {

    @Autowired
    private val mockMvc: MockMvc? = null
    @Test
    @Throws(Exception::class)
    fun noParamGreetingShouldReturnDefaultMessage() = runBlocking {

        LOGGER.info("GreetingControllerTests first() start => " + Thread.currentThread().name)
        Thread.sleep(500)

        mockMvc!!
            .perform(MockMvcRequestBuilders.get("/greeting"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("Hello, World!"))

        LOGGER.info("GreetingControllerTests first() end => " + Thread.currentThread().name)
    }

    @Test
    @Throws(Exception::class)
    fun paramGreetingShouldReturnTailoredMessage() = runBlocking {

        LOGGER.info("GreetingControllerTests second() start => " + Thread.currentThread().name)
        Thread.sleep(500)

        mockMvc!!
            .perform(MockMvcRequestBuilders.get("/greeting")
                .param("name", "Spring Community"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("Hello, Spring Community!"))


        LOGGER.info("GreetingControllerTests second() end => " + Thread.currentThread().name)
    }
}
