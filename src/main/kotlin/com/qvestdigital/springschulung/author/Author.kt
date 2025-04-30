package com.qvestdigital.springschulung.author

import com.qvestdigital.springschulung.books.Book
import com.qvestdigital.springschulung.books.BookRead
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.SEQUENCE
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.validation.constraints.NotBlank

@Entity(name = "AUTHORS")
data class Author(
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    val id: Long?,

    @Column(name = "NAME", nullable = false)
    @field:NotBlank
    val name: String,

    @Column(name = "SURNAME", nullable = false)
    @field:NotBlank
    val surname: String,

    @OneToMany(mappedBy = "author", cascade = [CascadeType.ALL], orphanRemoval = true)
    val books: List<Book> = mutableListOf()
)

data class AuthorWrite(
    val name: String,
    val surname: String
)

data class AuthorRead(
    val authorId: Long,
    val name: String,
    val surname: String,
) {
    companion object {
        fun from(author: Author): AuthorRead = AuthorRead(
            authorId = author.id ?: 0L,
            name = author.name,
            surname = author.surname,
        )
    }
}