package com.qvestdigital.examples.spring_di

import org.springframework.stereotype.Component

@Component
class UserService(private val databaseAccess: Database) {
    fun getCustomerByName(name: String) =
        databaseAccess.getCustomerName()

    fun findByName(id: String): Customer? = null

    fun findById(id: Long): Customer? = null
}