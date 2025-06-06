package com.qvestdigital.examples.spring_di

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.bind.Name
import org.springframework.stereotype.Component

@Component("MyBeanName")
class AddressService() {
    @Name("mainDatabaseAccess")
    @Autowired
    private lateinit var databaseAccess: Database

    val logger = KotlinLogging.logger {}

    init {
        logger.info { "Ich bin der Konstruktor" }
        logger.info("database is set = ${::databaseAccess.isInitialized}")
    }

    @PostConstruct
    fun initAfterConstruction() {
        logger.info { "Ich komme nach dem Konstruktor" }
        logger.info("database is set = ${::databaseAccess.isInitialized}")
    }

    @PreDestroy
    fun beforeDestroy() {
        logger.info { "Ich komme fast zum schluss." }
    }

    fun getAddressByName(name: String): String {
        logger.info { "Loading address by name $name" }
        return databaseAccess.getAddress()
    }
}
