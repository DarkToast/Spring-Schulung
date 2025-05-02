package com.qvestdigital.springschulung

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import mu.KotlinLogging
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ErrorHandler {
    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(exception: ConstraintViolationException): ResponseEntity<Any?> {
        logger.warn(exception.message, exception)
        val failures = exception.constraintViolations.map { Failure(it.message, it.propertyPath.toString()) }
        return ResponseEntity(failures, BAD_REQUEST)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(
        request: HttpServletRequest,
        exception: AccessDeniedException
    ): ResponseEntity<Any?> {

        logger.warn(exception.message, exception)
        val path = request.requestURI
        val failure = Failure(exception.message ?: "Malformed JSON", path)
        return ResponseEntity(failure, FORBIDDEN)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadable(
        request: HttpServletRequest,
        exception: HttpMessageNotReadableException
    ): ResponseEntity<Any?> {

        logger.warn(exception.message, exception)
        val path = request.requestURI
        val failure = Failure(exception.message ?: "Malformed JSON", path)
        return ResponseEntity(failure, BAD_REQUEST)
    }

}

data class Failure(val message: String, val path: String)