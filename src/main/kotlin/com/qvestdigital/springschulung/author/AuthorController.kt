package com.qvestdigital.springschulung.author

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
//import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
@RequestMapping("/api")
class AuthorController(val authorService: AuthorService) {

    @PostMapping("/authors")
    @PreAuthorize("hasRole('WRITE')")
    fun addAuthor(@RequestBody @Valid authorWrite: AuthorWrite, bindingResult: BindingResult): ResponseEntity<Author> {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build()
        }

        val author = Author(id = null, name = authorWrite.name, surname = authorWrite.surname)
        val savedAuthor = authorService.saveAuthor(author)
        return ResponseEntity.ok(savedAuthor)
    }

    @GetMapping("/authors")
    @PreAuthorize("hasRole('READ')")
    fun getAuthors(): ResponseEntity<List<AuthorRead>> =
        authorService.getAllAuthors().map { AuthorRead.from(it) }.run { ResponseEntity.ok(this) }

    @GetMapping("/authors/{id}")
    @PreAuthorize("hasRole('READ')")
    fun getAuthorById(@PathVariable id: Long): ResponseEntity<AuthorRead> {
        val author = authorService.getAuthorById(id)
        return if (author != null) {
            ResponseEntity.ok(AuthorRead.from(author))
        } else {
            ResponseEntity.status(NOT_FOUND).build()
        }
    }
}