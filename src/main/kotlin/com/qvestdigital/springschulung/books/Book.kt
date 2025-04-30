package com.qvestdigital.springschulung.books

import com.qvestdigital.springschulung.author.Author
import com.qvestdigital.springschulung.author.AuthorRead
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.SEQUENCE
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity(name = "BOOKS")
data class Book(
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    val id: Long?,

    @ManyToOne(cascade = [(CascadeType.MERGE)])
    @JoinColumn(name = "AUTHOR_ID", nullable = false)
    val author: Author,

    @Column(name = "TITLE", nullable = false)
    @field:NotBlank
    val title: String,

    @Column(name = "PUBLISHER", nullable = false)
    @field:NotBlank
    val publisher: String,

    @Column(name = "PUBLISHED") @field:NotNull
    val year: Int,

    @Column(name = "ISBN", unique = true, nullable = true)
    @field:Size(min = 10, max = 13)
    val ean: String?
    // EAN vs ISBN vs ISSN vs GTIN …
    // ISBN können weder als id verwendet, noch auf unique=true gesetzt werden.
    // SBN wurden in 1966 eingeführt, ISBN-10 in 1970, ISBN-13 in 2007.
    // Ausgaben vor 1966 haben somit keinen eindeutigen Identifier.
) {
    fun update(author: Author, write: BookWrite): Book = this.copy(
        author = author,
        title = write.title,
        publisher = write.publisher,
        year = write.year,
        ean = write.ean
    )

    companion object {
        fun create(author: Author, write: BookWrite): Book = Book(
            id = null,
            author = author,
            title = write.title,
            publisher = write.publisher,
            year = write.year,
            ean = write.ean
        )
    }

    fun toBookRead(): BookRead {
        return BookRead(
            id = this.id,
            author = AuthorRead(
                authorId = this.author.id!!,
                name = this.author.name,
                surname = this.author.surname
            ),
            title = this.title,
            publisher = this.publisher,
            year = this.year,
            ean = this.ean
        )
    }
}

data class BookWrite(
    val authorId: Long,
    val title: String,
    val publisher: String,
    val year: Int,
    val ean: String?
)

data class BookRead(
    val id: Long?,
    val author: AuthorRead,
    val title: String,
    val publisher: String,
    val year: Int,
    val ean: String?
)