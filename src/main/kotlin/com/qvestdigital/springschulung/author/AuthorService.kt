package com.qvestdigital.springschulung.author

import org.springframework.stereotype.Service

@Service
class AuthorService(val authorRepository: AuthorRepository) {

    fun saveAuthor(author: Author): Author = authorRepository.save(author)

    fun getAllAuthors(): List<Author> = authorRepository.findAll()

    fun getAuthorById(id: Long): Author? = authorRepository.findById(id).orElse(null)
}