package com.qvestdigital.springschulung.author

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class AuthorController(val authorService: AuthorService) {

    @PostMapping("/authors")
    fun addAuthor(@RequestBody authorWrite: AuthorWrite): ResponseEntity<Author> {
        val author = Author(id = null, name = authorWrite.name, surname = authorWrite.surname)
        val savedAuthor = authorService.saveAuthor(author)
        return ResponseEntity.ok(savedAuthor)
    }

    @GetMapping("/authors")
    fun getAuthors(): ResponseEntity<List<AuthorRead>> =
        authorService.getAllAuthors().map { AuthorRead.from(it) }.run { ResponseEntity.ok(this) }

    @GetMapping("/authors/{id}")
    fun getAuthorById(@PathVariable id: Long): ResponseEntity<AuthorRead> {
        val author = authorService.getAuthorById(id)
        return if (author != null) {
            ResponseEntity.ok(AuthorRead.from(author))
        } else {
            ResponseEntity.status(NOT_FOUND).build()
        }
    }
}