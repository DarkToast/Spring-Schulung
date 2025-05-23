package com.qvestdigital.springschulung.author

import com.qvestdigital.springschulung.books.Book
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.SEQUENCE
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

@Entity(name = "AUTHORS")
data class Author(
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    val id: Long?,

    @Column(name = "NAME", nullable = false)
    @field:NotBlank
    @field:Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
    val name: String,

    @Column(name = "SURNAME", nullable = false)
    @field:NotBlank
    @field:Size(min = 2, max = 30, message = "Surname must be between 2 and 30 characters")
    val surname: String,

    @OneToMany(mappedBy = "author", cascade = [CascadeType.ALL], orphanRemoval = true)
    val books: List<Book> = mutableListOf()
)

@ValidAuthor
data class AuthorWrite(
    @field:NotEmpty(message = "Name cannot be empty")
    @field:Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
    val name: String,

    @field:NotEmpty(message = "Surname cannot be empty")
    @field:Size(min = 2, max = 30, message = "Surname must be between 2 and 30 characters")
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