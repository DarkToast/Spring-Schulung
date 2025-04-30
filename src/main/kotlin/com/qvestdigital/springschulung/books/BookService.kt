package com.qvestdigital.springschulung.books

import com.qvestdigital.springschulung.author.AuthorRepository
import com.qvestdigital.springschulung.books.Failure.EanAlreadyInUse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookService(val bookRepository: BookRepository, val authorRepository: AuthorRepository) {

    fun getBook(id: Long): Book? = bookRepository.findById(id).orElse(null)

    fun saveBook(bookModel: BookWrite): Book? {
        if(bookModel.ean != null) {
            if(bookRepository.existsByEan(bookModel.ean)) {
                throw ServiceException(EanAlreadyInUse(bookModel.ean))
            }
        }

        val author = authorRepository.findById(bookModel.authorId).orElse(null) ?: return null
        val book = Book.create(author, bookModel)
        return bookRepository.save(book)
    }

    @Transactional
    fun updateBook(id: Long, bookModel: BookWrite): Book? {
        val author = authorRepository.findById(bookModel.authorId).orElse(null) ?: return null
        val existingBook = bookRepository.findById(id).orElse(null) ?: return null
        val updatedBook = existingBook.update(author, bookModel)
        return bookRepository.save(updatedBook)
    }

    fun deleteBook(id: Long): Boolean {
        if(!bookRepository.existsById(id)) {
            return false
        }

        bookRepository.deleteById(id)
        return true
    }

    fun getBooksOlderThan(cutoffYear: Int): List<Book> =
        bookRepository.findBooksOlderThan(cutoffYear)

    fun searchBooks(author: String?, year: Int?): List<Book> =
        bookRepository.findByAuthorAndYear(author, year)
}