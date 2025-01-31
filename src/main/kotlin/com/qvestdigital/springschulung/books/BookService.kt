package com.qvestdigital.springschulung.books

import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class BookService(val repository: BookRepository) {
    fun getAllBooks(): List<Book> = repository.findAll()

    fun getBook(id: Long): Book? = repository.findById(id).getOrNull()

    fun saveBook(book: Book): Book = repository.save(book)
}
