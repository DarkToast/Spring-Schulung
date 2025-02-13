package com.qvestdigital.springschulung.books

import jakarta.validation.Valid
import java.net.URI
import java.time.LocalDate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class BookController(val service: BookService) {

    @GetMapping("/books/{id}")
    fun getBook(@PathVariable id: Long): ResponseEntity<Book> =
        service.getBook(id)
            ?.let { ResponseEntity.ok().body(it) }
            ?: ResponseEntity.notFound().build()

    @PostMapping("/books")
    fun addBook(@Valid @RequestBody book: Book): ResponseEntity<Book> =
        service.saveBook(book).let {
            ResponseEntity.created(URI.create("/api/books/${it.id}")).body(it)
        }

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