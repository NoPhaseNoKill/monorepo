package com.integraboost.controller

import com.integraboost.IntegrationTest
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
class GreetingController2Tests: IntegrationTest() {

    @Autowired
    private val mockMvc: MockMvc? = null
    @Test
    @Throws(Exception::class)
    fun noParamGreetingShouldReturnDefaultMessage() {
        Thread.sleep(10000)
        println("sleeping thread 10000");
        mockMvc!!.perform(MockMvcRequestBuilders.get("/greeting")).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("Hello, World!"))
    }

    @Test
    @Throws(Exception::class)
    fun paramGreetingShouldReturnTailoredMessage() {
        mockMvc!!.perform(MockMvcRequestBuilders.get("/greeting").param("name", "Spring Community"))
            .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("Hello, Spring Community!"))
    }
}
