package com.qvestdigital.springschulung.books

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.*
import jakarta.persistence.Id

@Entity(name = "BOOKS")
class Book(
    @Id @GeneratedValue(strategy = SEQUENCE) val id: Long,
    @Column(name = "AUTHOR", nullable = false) val author: String,
    @Column(name = "TITLE", nullable = false) val title: String,
    @Column(name = "PUBLISHER", nullable = false) val publisher: String,
    @Column(name = "PUBLISHED") val year: Int,
    @Column(name = "ISBN") val ean: String?

    // EAN vs ISBN vs ISSN vs GTIN …
    // ISBN können weder als id verwendet, noch auf unique=true gesetzt werden.
    // SBN wurden in 1966 eingeführt, ISBN-10 in 1970, ISBN-13 in 2007.
    // Ausgaben vor 1966 haben somit keinen eindeutigen Identifier.
)