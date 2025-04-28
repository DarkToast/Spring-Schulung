package com.qvestdigital.springschulung.books

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.*
import jakarta.persistence.Id
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity(name = "BOOKS")
data class Book(
    @Id @GeneratedValue(strategy = SEQUENCE) val id: Long?,
    @Column(name = "AUTHOR", nullable = false) @field:NotBlank val author: String,
    @Column(name = "TITLE", nullable = false) @field:NotBlank val title: String,
    @Column(name = "PUBLISHER", nullable = false) @field:NotBlank val publisher: String,
    @Column(name = "PUBLISHED") @field:NotNull val year: Int,
    @Column(name = "ISBN", unique = true, nullable = true) @field:Size(min = 10, max = 13) val ean: String?
    // EAN vs ISBN vs ISSN vs GTIN …
    // ISBN können weder als id verwendet, noch auf unique=true gesetzt werden.
    // SBN wurden in 1966 eingeführt, ISBN-10 in 1970, ISBN-13 in 2007.
    // Ausgaben vor 1966 haben somit keinen eindeutigen Identifier.
)