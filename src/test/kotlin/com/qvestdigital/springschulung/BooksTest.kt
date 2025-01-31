package com.qvestdigital.springschulung

import com.qvestdigital.springschulung.books.Book
import com.qvestdigital.springschulung.books.BookRepository
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.*
import org.springframework.boot.test.autoconfigure.web.servlet.*
import org.springframework.boot.test.context.*
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*
import org.springframework.http.*
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
class BooksTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var bookRepository: BookRepository

    @AfterEach()
    fun tearDown() {
        bookRepository.deleteAll()
    }

    @Test
    fun listIsEmpty() {
        mockMvc.perform(get("/api/books"))
            .andExpect(status().isOk)
            .andExpectAll(
                jsonPath("$").exists(),
                jsonPath("$").isArray(),
                jsonPath("$").isEmpty()
            )
    }

    @Test
    fun listHasTwoBooks() {
        createBook(Book(null, "Author 1", "Book Title 1", "Publisher 1", 2025, "123467890"))
        createBook(Book(null, "Author 2", "Book Title 2", "Publisher 2", 2025, "0987654321"))

        mockMvc.perform(get("/api/books"))
            .andExpect(status().isOk)
            .andExpect {
                jsonPath("$").exists()
                jsonPath("$").isArray()
                jsonPath("$").isNotEmpty()

                jsonPath("$[0].author").value("Author 1")
                jsonPath("$[0].title").value("Book Title 1")
                jsonPath("$[0].publisher").value("Publisher 1")
                jsonPath("$[0].year").value(2025)
                jsonPath("$[0].ean").value("123467890")

                jsonPath("$[1].author").value("Author 2")
                jsonPath("$[1].title").value("Book Title 2")
                jsonPath("$[1].publisher").value("Publisher 2")
                jsonPath("$[1].year").value(2025)
                jsonPath("$[1].ean").value("0987654321")
            }
    }

    @Test
    fun addBook() {
        val bookJson = """
            {
                "author": "Author Name",
                "title": "Book Title",
                "publisher": "Publisher Name",
                "year": 2023,
                "ean": "1234567890123"
            }
        """.trimIndent()

        val post = post("/api/books").contentType(MediaType.APPLICATION_JSON).content(bookJson)
        mockMvc.perform(post).andExpect(status().isOk)
    }

    fun createBook(book: Book) {
        val bookJson = """
            {
                "author": "${book.author}",
                "title": "${book.title}",
                "publisher": "${book.publisher}",
                "year": ${book.year},
                "ean": "${book.ean}"
            }
        """.trimIndent()

        val post = post("/api/books").contentType(MediaType.APPLICATION_JSON).content(bookJson)
        mockMvc.perform(post).andExpect(status().isOk)
    }

}