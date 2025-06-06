package com.qvestdigital.springschulung.books

import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(ServiceException::class)
    fun handleServiceException(ex: ServiceException): ResponseEntity<String> {
        return when (ex.errorCode) {
            1 -> ResponseEntity.status(CONFLICT).body(ex.message)
            else -> ResponseEntity.status(INTERNAL_SERVER_ERROR).body("An unexpected error occurred")
        }
    }
}