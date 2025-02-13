package com.qvestdigital.springschulung.books

import org.springframework.stereotype.Service

@Service
class BookService(val repository: BookRepository) {

    fun getBook(id: Long): Book? = repository.findById(id).orElse(null)

    fun saveBook(book: Book): Book {
        if (repository.existsByEan(book.ean ?: "")) {
            throw ServiceException.EanAlreadyInUse
        }
        return repository.save(book)
    }

    fun updateBook(id: Long, book: Book): Book? =
        repository.findById(id).orElse(null)?.let {
            val updatedBook = it.copy(
                author = book.author,
                title = book.title,
                publisher = book.publisher,
                year = book.year,
                ean = book.ean
            )
            repository.save(updatedBook)
        }

    fun deleteBook(id: Long): Boolean {
        if(!repository.existsById(id)) {
            return false
        }

        repository.deleteById(id)
        return true
    }

    fun getBooksOlderThan(cutoffYear: Int): List<Book> =
        repository.findBooksOlderThan(cutoffYear)

    fun searchBooks(author: String?, year: Int?): List<Book> =
        repository.findByAuthorAndYear(author, year)
}