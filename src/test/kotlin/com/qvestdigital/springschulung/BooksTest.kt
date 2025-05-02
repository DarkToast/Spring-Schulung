package com.qvestdigital.springschulung.books

import com.qvestdigital.springschulung.author.Author
import com.qvestdigital.springschulung.author.AuthorRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.request.RequestPostProcessor
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Year.now
import javax.sql.DataSource

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
class BooksTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var bookRepo: BookRepository

    @Autowired
    lateinit var authorRepo: AuthorRepository

    @Autowired
    lateinit var dataSource: DataSource

    @BeforeEach
    fun setup() {
        bookRepo.deleteAll()
        authorRepo.deleteAll()
        createAndSaveAuthorsAndBooks()
    }

    private fun createAndSaveAuthorsAndBooks(): List<Book> {
        val now = now().value

        val authorOne = authorRepo.save(Author(id = null, name = "Author One", surname = "Author One Name"))
        val authorTwo = authorRepo.save(Author(id = null, name = "Author Two", surname = "Author Two Name"))
        val authorThree = authorRepo.save(Author(id = null, name = "Author Three", surname = "Author Three Name"))

        val books = listOf(
            Book(id = null, author = authorOne, title = "Book One", publisher = "Publisher One", year = now, ean = "1234567890123"),
            Book(id = null, author = authorTwo, title = "Book Two", publisher = "Publisher Two", year = now - 1, ean = "1234567890124"),
            Book(id = null, author = authorOne, title = "Book Three", publisher = "Publisher One", year = now - 2, ean = "1234567890125"),
            Book(id = null, author = authorThree, title = "Book Four", publisher = "Publisher Three", year = now - 3, ean = "1234567890126"),
            Book(id = null, author = authorTwo, title = "Book Five", publisher = "Publisher Two", year = now - 3, ean = "1234567890127")
        )
        return bookRepo.saveAll(books)
    }

    private fun getYearOlderThan(years: Int): Int {
        return now().value - years
    }

    fun writer(): RequestPostProcessor {
        return user("writer").roles("WRITE", "READ")
    }

    @Test
    fun getAllBooksReturnsListWithBooks() {
        mockMvc.perform(get("/api/books").with(writer()))
            .andExpect(status().isOk)
            .andExpectAll(
                jsonPath("$").exists(),
                jsonPath("$").isArray(),
                jsonPath("$.length()").value(5)
            )
    }

    @Test
    fun getBookByIdReturnsBook() {
        val book = bookRepo.findAll().first()
        mockMvc.perform(get("/api/books/${book.id}").with(writer()))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(book.id))
            .andExpect(jsonPath("$.author.name").value(book.author.name))
            .andExpect(jsonPath("$.title").value(book.title))
    }

    @Test
    fun getBookByIdReturnsNotFound() {
        mockMvc.perform(get("/api/books/999").with(writer()))
            .andExpect(status().isNotFound)
    }

    @Test
    fun addBookReturnsCreatedBook() {
        val author = authorRepo.queryAuthorsByName("Author Two").first()

        val bookJson = """
            {
                "authorId": ${author.id},
                "title": "Book Title",
                "publisher": "Publisher Name",
                "year": 2023,
                "ean": "1234567890128"
            }
        """.trimIndent()

        mockMvc.perform(
            post("/api/books")
                .with(writer())
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson)
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.author.name").value("Author Two"))
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
                .with(writer())
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidBookJson)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun saveBookWithEanAlreadyInUse() {
        val author = authorRepo.queryAuthorsByName("Author Two").first()

        val duplicateBookJson = """
            {
                "authorId": ${author.id},
                "title": "Another Book Title",
                "publisher": "Another Publisher Name",
                "year": 2024,
                "ean": "1234567890123"
            }
        """.trimIndent()

        mockMvc.perform(
            post("/api/books")
                .with(writer())
                .contentType(MediaType.APPLICATION_JSON)
                .content(duplicateBookJson)
        )
            .andExpect(status().isConflict)
    }

    @Test
    fun getBooksByAuthorReturnsListWithBooks() {
        mockMvc.perform(get("/api/books?author=Author One").with(writer()))
            .andExpect(status().isOk)
            .andExpectAll(
                jsonPath("$").exists(),
                jsonPath("$").isArray(),
                jsonPath("$.length()").value(2),
                jsonPath("$[0].author.name").value("Author One"),
                jsonPath("$[1].author.name").value("Author One")
            )
    }

    @Test
    fun getBooksByAuthorReturnsEmptyList() {
        mockMvc.perform(get("/api/books?author=Nonexistent Author").with(writer()))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty())
    }

    @Test
    fun getBooksByYearReturnsListWithBooks() {
        val year = getYearOlderThan(3)
        mockMvc.perform(get("/api/books?year=$year").with(writer()))
            .andExpect(status().isOk)
            .andExpectAll(
                jsonPath("$").exists(),
                jsonPath("$").isArray(),
                jsonPath("$.length()").value(2),
                jsonPath("$[0].year").value(2022),
                jsonPath("$[0].author.name").value("Author Three"),
                jsonPath("$[1].year").value(2022),
                jsonPath("$[1].author.name").value("Author Two")
            )
    }

    @Test
    fun getBooksByYearReturnsEmptyList() {
        mockMvc.perform(get("/api/books?year=5").with(writer()))
            .andExpect(status().isOk)
            .andExpectAll(jsonPath("$").isEmpty())
    }

    @Test
    fun getBooksOlderThanReturnsListWithBooks() {
        mockMvc.perform(get("/api/books?older-than=2").with(writer()))
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
        mockMvc.perform(get("/api/books?author=Author Three&year=$year").with(writer()))
            .andExpect(status().isOk)
            .andExpectAll(
                jsonPath("$").exists(),
                jsonPath("$").isArray(),
                jsonPath("$.length()").value(1),
                jsonPath("$[0].author.name").value("Author Three"),
                jsonPath("$[0].year").value(2022)
            )
    }

    @Test
    fun searchBooksReturnsEmptyList() {
        val year = getYearOlderThan(0)
        mockMvc.perform(get("/api/books?author=Nonexistent Author&year=$year").with(writer()))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty())
    }

    @Test
    fun updateBookReturnsUpdatedBook() {
        val book = bookRepo.findAll().first()
        val author = authorRepo.queryAuthorsByName("Author Two").first()

        val updatedBookJson = """
            {
                "authorId": ${author.id},
                "title": "Updated Title",
                "publisher": "Updated Publisher",
                "year": 2023,
                "ean": "1234567890123"
            }
        """.trimIndent()

        mockMvc.perform(
            put("/api/books/${book.id}")
                .with(writer())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedBookJson)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(book.id))
            .andExpect(jsonPath("$.author.name").value("Author Two"))
            .andExpect(jsonPath("$.title").value("Updated Title"))
    }

    @Test
    fun updateBookReturnsNotFound() {
        val updatedBookJson = """
            {
                "author": 2,
                "title": "Updated Title",
                "publisher": "Updated Publisher",
                "year": 2023,
                "ean": "1234567890123"
            }
        """.trimIndent()

        mockMvc.perform(
            put("/api/books/999")
                .with(writer())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedBookJson)
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun deleteBookReturnsNoContent() {
        val book = bookRepo.findAll().first()

        mockMvc.perform(delete("/api/books/${book.id}").with(writer()))
            .andExpect(status().isNoContent)

        mockMvc.perform(get("/api/books/${book.id}").with(writer()))
            .andExpect(status().isNotFound)
    }

    @Test
    fun deleteBookReturnsNotFound() {
        mockMvc.perform(delete("/api/books/999").with(writer()))
            .andExpect(status().isNotFound)
    }
}