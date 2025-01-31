package com.qvestdigital.springschulung.books

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
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

    @PostMapping("/books")
    fun addBook(@RequestBody book: Book): ResponseEntity<Book> {
        service.saveBook(book)
        return ResponseEntity.ok().build()
    }
}