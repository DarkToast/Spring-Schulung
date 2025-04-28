package com.qvestdigital.springschulung.books

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : JpaRepository<Book, Long> {
    @Query("SELECT b FROM BOOKS b WHERE (:author IS NULL OR b.author.name = :author) AND (:year IS NULL OR b.year = :year)")
    fun findByAuthorAndYear(@Param("author") author: String?, @Param("year") year: Int?): List<Book>

    @Query("SELECT b FROM BOOKS b WHERE b.year < :cutoffYear")
    fun findBooksOlderThan(@Param("cutoffYear") cutoffYear: Int): List<Book>

    fun existsByEan(ean: String): Boolean
}