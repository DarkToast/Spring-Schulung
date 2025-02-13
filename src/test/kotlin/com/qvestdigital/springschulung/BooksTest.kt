package com.qvestdigital.springschulung.books

import java.time.Year.now
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
class BookControllerIntegrationTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var repository: BookRepository

    @BeforeEach
    fun setup() {
        repository.deleteAll()
        createAndSaveBooks()
    }

    private fun createAndSaveBooks(): List<Book> {
        val now = now().value
        val books = listOf(
            Book(id = null, author = "Author One", title = "Book One", publisher = "Publisher One", year = now, ean = "1234567890123"),
            Book(id = null, author = "Author Two", title = "Book Two", publisher = "Publisher Two", year = now - 1, ean = "1234567890124"),
            Book(id = null, author = "Author One", title = "Book Three", publisher = "Publisher One", year = now - 2, ean = "1234567890125"),
            Book(id = null, author = "Author Three", title = "Book Four", publisher = "Publisher Three", year = now - 3, ean = "1234567890126"),
            Book(id = null, author = "Author Two", title = "Book Five", publisher = "Publisher Two", year = now - 3, ean = "1234567890127")
        )
        return repository.saveAll(books)
    }

    private fun getYearOlderThan(years: Int): Int {
        return now().value - years
    }

    @Test
    fun getAllBooksReturnsListWithBooks() {
        mockMvc.perform(get("/api/books"))
            .andExpect(status().isOk)
            .andExpectAll(
                jsonPath("$").exists(),
                jsonPath("$").isArray(),
                jsonPath("$.length()").value(5)
            )
    }

    @Test
    fun getBookByIdReturnsBook() {
        val book = repository.findAll().first()
        mockMvc.perform(get("/api/books/${book.id}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(book.id))
            .andExpect(jsonPath("$.author").value(book.author))
            .andExpect(jsonPath("$.title").value(book.title))
    }

    @Test
    fun getBookByIdReturnsNotFound() {
        mockMvc.perform(get("/api/books/999"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun addBookReturnsCreatedBook() {
        val bookJson = """
            {
                "author": "Author Name",
                "title": "Book Title",
                "publisher": "Publisher Name",
                "year": 2023,
                "ean": "1234567890123"
            }
        """.trimIndent()

        mockMvc.perform(
            post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson)
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.author").value("Author Name"))
            .andExpect(jsonPath("$.title").value("Book Title"))
    }

    @Test
    fun addInvalidBookReturnsBadRequest() {
        val invalidBookJson = """
            {
                "author": "Author Name"
            }
        """.trimIndent()

        mockMvc.perform(
            post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidBookJson)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun getBooksByAuthorReturnsListWithBooks() {
        mockMvc.perform(get("/api/books?author=Author One"))
            .andExpect(status().isOk)
            .andExpectAll(
                jsonPath("$").exists(),
                jsonPath("$").isArray(),
                jsonPath("$.length()").value(2),
                jsonPath("$[0].author").value("Author One"),
                jsonPath("$[1].author").value("Author One")
            )
    }

    @Test
    fun getBooksByAuthorReturnsEmptyList() {
        mockMvc.perform(get("/api/books?author=Nonexistent Author"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty())
    }

    @Test
    fun getBooksByYearReturnsListWithBooks() {
        val year = getYearOlderThan(3)
        mockMvc.perform(get("/api/books?year=$year"))
            .andExpect(status().isOk)
            .andExpectAll(
                jsonPath("$").exists(),
                jsonPath("$").isArray(),
                jsonPath("$.length()").value(2),
                jsonPath("$[0].year").value(2022),
                jsonPath("$[0].author").value("Author Three"),
                jsonPath("$[1].year").value(2022),
                jsonPath("$[1].author").value("Author Two")
            )
    }

    @Test
    fun getBooksByYearReturnsEmptyList() {
      mockMvc.perform(get("/api/books?year=5"))
            .andExpect(status().isOk)
            .andExpectAll(jsonPath("$").isEmpty())
    }

    @Test
    fun getBooksOlderThanReturnsListWithBooks() {
        mockMvc.perform(get("/api/books?older-than=2"))
            .andExpect(status().isOk)
            .andExpectAll(
                jsonPath("$").exists(),
                jsonPath("$").isArray(),
                jsonPath("$.length()").value(2)
            )
    }

    @Test
    fun searchBooksByAuthorAndYearReturnsListWithBooks() {
        val year = getYearOlderThan(3)
        mockMvc.perform(get("/api/books?author=Author Three&year=$year"))
            .andExpect(status().isOk)
            .andExpectAll(
                jsonPath("$").exists(),
                jsonPath("$").isArray(),
                jsonPath("$.length()").value(1),
                jsonPath("$[0].author").value("Author Three"),
                jsonPath("$[0].year").value(2022)
            )
    }

    @Test
    fun searchBooksReturnsEmptyList() {
        val year = getYearOlderThan(0)
        mockMvc.perform(get("/api/books?author=Nonexistent Author&year=$year"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty())
    }
}