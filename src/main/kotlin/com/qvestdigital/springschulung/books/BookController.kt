package com.qvestdigital.springschulung.books

import com.qvestdigital.springschulung.author.AuthorRead
import com.qvestdigital.springschulung.books.Failure.ResourceNotFound
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.time.LocalDate

@RestController
@RequestMapping("/api")
class BookController(val service: BookService) {

    @Operation(summary = "Get a book by its ID")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Found the book",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = BookRead::class))]
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
    @PreAuthorize("hasRole('READ')")
    @GetMapping("/books/{id}")
    fun getBook(@PathVariable id: Long): ResponseEntity<*> =
        service.getBook(id)
            ?.let { book ->
                val bookRead = book.toBookRead()
                ResponseEntity.ok().body(bookRead)
            }
            ?: ResponseEntity.status(NOT_FOUND).body(ResourceNotFound("book", id))


    @Operation(summary = "Add a new book")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Book created",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = BookRead::class))]
            ),
            ApiResponse(responseCode = "400", description = "Invalid input", content = [])
        ]
    )
    @PreAuthorize("hasRole('WRITE')")
    @PostMapping("/books")
    fun addBook(@Valid @RequestBody bookModel: BookWrite): ResponseEntity<*> {
        val savedBook = service.saveBook(bookModel) ?: return ResponseEntity.status(NOT_FOUND)
            .body(ResourceNotFound("author", bookModel.authorId))
        val bookRead = savedBook.toBookRead()
        return ResponseEntity.created(URI.create("/api/books/${bookRead.id}")).body(bookRead)
    }


    @Operation(summary = "Update an existing book")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Book updated",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = BookRead::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Book or author not found",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ResourceNotFound::class)
                )]
            )
        ]
    )
    @PreAuthorize("hasRole('WRITE')")
    @PutMapping("/books/{id}")
    fun updateBook(@PathVariable id: Long, @Valid @RequestBody bookModel: BookWrite): ResponseEntity<*> {
        val updatedBook = service.updateBook(id, bookModel) ?: return ResponseEntity.status(NOT_FOUND)
            .body(ResourceNotFound("book or author", id))
        val bookRead = updatedBook.toBookRead()
        return ResponseEntity.ok().body(bookRead)
    }


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
    @PreAuthorize("hasRole('WRITE')")
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
                content = [Content(mediaType = "application/json", schema = Schema(implementation = BookRead::class))]
            )
        ]
    )


    @GetMapping("/books")
    @PreAuthorize("hasRole('READ')")
    fun getBooks(
        @RequestParam(required = false) author: String?,
        @RequestParam(required = false) year: Int?,
        @RequestParam(required = false, name = "older-than") olderThan: Int?
    ): ResponseEntity<List<BookRead>> {
        val books = when {
            olderThan != null -> service.getBooksOlderThan(LocalDate.now().year - olderThan)
            else -> service.searchBooks(author, year)
        }
        val bookReads = books.map { it.toBookRead() }
        return ResponseEntity.ok().body(bookReads)
    }
}