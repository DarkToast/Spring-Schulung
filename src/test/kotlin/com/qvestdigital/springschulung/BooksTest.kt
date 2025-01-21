package com.qvestdigital.springschulung

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
class BooksTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun loadBooks() {
        mockMvc.perform(get("/api/books"))
            .andExpect(status().isOk)
            .andExpectAll(
                jsonPath("$").exists(),
                jsonPath("$").isArray(),
                jsonPath("$").isEmpty()
            )
    }

}