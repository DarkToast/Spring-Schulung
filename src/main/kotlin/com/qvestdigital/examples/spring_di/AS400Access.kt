package com.qvestdigital.examples.spring_di

import mu.KotlinLogging

class AS400Access(url: String, username: String, password: String): Database {
    private val logger = KotlinLogging.logger {}

    init {
        logger.info("Database connection established with URL: $url on as400.")
    }

    override fun getCustomerName(): String {
        logger.info("Database access to customer")
        return "Max Mustermann"
    }

    override fun getAddress(): String {
        logger.info("Address access to customer")
        return "Musterstraße 1, 12345 Musterstadt"
    }
}