package com.qvestdigital.springschulung

import jakarta.validation.ConstraintViolationException
import mu.KotlinLogging
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ErrorHandler {
    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(ConstraintViolationException::class)
    fun handlePathVariableError(exception: ConstraintViolationException): ResponseEntity<Any?> {
        logger.warn(exception.message, exception)
        val failures = exception.constraintViolations.map { Failure(it.message, it.propertyPath.toString()) }
        return ResponseEntity(failures, BAD_REQUEST)
    }
}

data class Failure(val message: String, val path: String)