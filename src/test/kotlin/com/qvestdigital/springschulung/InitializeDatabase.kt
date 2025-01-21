package com.qvestdigital.springschulung

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.goebl.david.Webb
import com.qvestdigital.springschulung.books.Book
import org.json.JSONObject
import java.io.File

fun main() {
    val webb = Webb.create()
        .apply { baseUri = "http://localhost:8080" }

    csvReader().open(File("./src/test/resources/books.csv")) {
        readAllAsSequence()
            .drop(1) // Header
            .map { (author, title, publish, year, ean) ->
                Book(0, author, title, publish, year.toInt(), ean.ifEmpty { null })
            }
            .map(::JSONObject) // Needed for Request body
            .forEach {
                webb.post("/api/book")
                    .body(it)
                    .ensureSuccess()
                    .asString()
            }
    }
}
