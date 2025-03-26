package com.qvestdigital.springschulung.author

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorRepository : JpaRepository<Author, Long> {
    fun queryAuthorsByName(name: String): MutableList<Author>
}