package com.qvestdigital.examples.spring_di

import mu.KotlinLogging
import org.springframework.stereotype.Service

class DatabaseAccess(url: String, username: String, password: String) {
    private val logger = KotlinLogging.logger {}

    init {
        logger.info("Database connection established with URL: $url")
    }

    fun getCustomerName(): String {
        logger.info("Database access to customer")
        return "Max Mustermann"
    }

    fun getAddress(): String {
        logger.info("Address access to customer")
        return "Musterstraße 1, 12345 Musterstadt"
    }
}