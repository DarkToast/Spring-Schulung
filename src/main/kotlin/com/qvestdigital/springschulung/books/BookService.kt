package com.qvestdigital.springschulung.books

import org.springframework.stereotype.Service

@Service
class BookService(val repository: BookRepository) {
    fun getAllBooks(): List<Book> = repository.findAll()
    fun saveBook(book: Book) = repository.save(book)
}
