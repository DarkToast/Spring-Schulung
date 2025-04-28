package com.qvestdigital.springschulung.books

import com.qvestdigital.springschulung.books.Failure.ResourceNotFound
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import java.net.URI
import java.time.LocalDate
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class BookController(val service: BookService) {

    @Operation(summary = "Get a book by its ID")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Found the book",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = Book::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Book not found",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ResourceNotFound::class)
                )]
            )
        ]
    )
    @GetMapping("/books/{id}")
    fun getBook(@PathVariable id: Long): ResponseEntity<*> =
        service.getBook(id)
            ?.let { ResponseEntity.ok().body(it) }
            ?: ResponseEntity.status(NOT_FOUND).body(ResourceNotFound("book", id))

    @Operation(summary = "Add a new book")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Book created",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = Book::class))]
            ),
            ApiResponse(responseCode = "400", description = "Invalid input", content = [])
        ]
    )
    @PostMapping("/books")
    fun addBook(@Valid @RequestBody book: Book): ResponseEntity<Book> =
        service.saveBook(book).let {
            ResponseEntity.created(URI.create("/api/books/${it.id}")).body(it)
        }

    @Operation(summary = "Update an existing book")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Book updated",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = Book::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Book not found",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ResourceNotFound::class)
                )]
            )
        ]
    )
    @PutMapping("/books/{id}")
    fun updateBook(@PathVariable id: Long, @Valid @RequestBody book: Book): ResponseEntity<*> =
        service.updateBook(id, book)
            ?.let { ResponseEntity.ok().body(it) }
            ?: ResponseEntity.status(NOT_FOUND).body(ResourceNotFound("book", id))

    @Operation(summary = "Delete a book by its ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Book deleted", content = []),
            ApiResponse(
                responseCode = "404",
                description = "Book not found",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ResourceNotFound::class)
                )]
            )
        ]
    )
    @DeleteMapping("/books/{id}")
    fun deleteBook(@PathVariable id: Long): ResponseEntity<*> =
        if (service.deleteBook(id)) ResponseEntity.noContent().build<Any>()
        else ResponseEntity.status(NOT_FOUND).body(ResourceNotFound("book", id))

    @Operation(summary = "Get books by author, year, or older than a certain number of years")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Found books",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = Book::class))]
            )
        ]
    )
    @GetMapping("/books")
    fun getBooks(
        @RequestParam(required = false) author: String?,
        @RequestParam(required = false) year: Int?,
        @RequestParam(required = false, name = "older-than") olderThan: Int?
    ): ResponseEntity<List<Book>> {
        val books = when {
            olderThan != null -> service.getBooksOlderThan(LocalDate.now().year - olderThan)
            else -> service.searchBooks(author, year)
        }
        return ResponseEntity.ok().body(books)
    }
}