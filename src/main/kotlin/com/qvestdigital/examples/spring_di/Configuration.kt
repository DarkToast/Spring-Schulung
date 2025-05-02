package com.qvestdigital.examples.spring_di

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@ConfigurationProperties(prefix = "external-service-config")
class ExternalServiceConfig {
    lateinit var url: String
    lateinit var credentials: Credentials
    lateinit var parameters: List<String>
}

class Credentials {
    lateinit var username: String
    lateinit var password: String
}

@ConfigurationProperties(prefix = "spring.datasource")
class DatabaseProperties() {
    lateinit var url: String
    lateinit var username: String
    lateinit var password: String
}

@Configuration
class Configuration {

    @Value("\${spring.datasource.url}")
    private lateinit var dbUrl: String

    @Value("\${spring.datasource.username}")
    private lateinit var dbUsername: String

    @Value("\${spring.datasource.password}")
    private lateinit var dbPassword: String

    @Autowired
    private lateinit var databaseProps: DatabaseProperties


    @Bean("as400Access")
    @Suppress("UnusedVariable", "unused")
    fun as400Access(): Database {
        val altDb = AS400Access(
            databaseProps.url, databaseProps.username, databaseProps.password
        )

        return AS400Access(dbUrl, dbUsername, dbPassword)
    }

    @Bean("postgresAccess")
    fun postgresAccess(): Database {
        return PostgresAccess(dbUrl, dbUsername, dbPassword)
    }
}