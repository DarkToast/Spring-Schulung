package com.qvestdigital.springschulung.books

import java.net.URI
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class BookController(val service: BookService) {

    @GetMapping("/books")
    fun getAllBooks(): ResponseEntity<List<Book>> =
        ResponseEntity.ok().body(service.getAllBooks())

    @GetMapping("/books/{id}")
    fun getAllBooks(@PathVariable id: Long): ResponseEntity<Book> =
        service.getBook(id)
            ?.let { ResponseEntity.ok().body(it) }
            ?: ResponseEntity.notFound().build()

    @PostMapping("/books")
    fun addBook(@RequestBody book: Book): ResponseEntity<Book> =
        service.saveBook(book).let {
            ResponseEntity.created(URI.create("/api/books/${it.id}")).body(it)
        }
}