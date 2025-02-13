package com.qvestdigital.springschulung.books

class ServiceException private constructor(val errorCode: Int, message: String) : RuntimeException(message) {
    companion object {
        val EanAlreadyInUse = ServiceException(1, "The EAN is already in use")
    }
}

