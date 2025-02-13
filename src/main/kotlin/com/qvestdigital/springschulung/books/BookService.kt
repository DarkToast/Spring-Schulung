package com.qvestdigital.springschulung.books

import org.springframework.stereotype.Service

@Service
class BookService(val repository: BookRepository) {

    fun getBook(id: Long): Book? = repository.findById(id).orElse(null)

    fun saveBook(book: Book): Book = repository.save(book)

    fun getBooksOlderThan(cutoffYear: Int): List<Book> =
        repository.findBooksOlderThan(cutoffYear)

    fun searchBooks(author: String?, year: Int?): List<Book> =
        repository.findByAuthorAndYear(author, year)
}