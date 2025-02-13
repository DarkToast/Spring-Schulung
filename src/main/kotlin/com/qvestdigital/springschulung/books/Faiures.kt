package com.qvestdigital.springschulung.books

class ServiceException(failure: Failure) : RuntimeException(failure.message) {
    val errorCode = failure.errorCode
}

@Suppress("unused", "MemberVisibilityCanBePrivate")
sealed class Failure(val errorCode: Int, val message: String) {
    data class EanAlreadyInUse(val ean: String?) : Failure(1, "The EAN is already in use")
    data class ResourceNotFound(val type: String, val id: Long) : Failure(2, "The $type with id $id was not found")
}

