package com.qvestdigital.examples.di

class DI {
}

class DatabaseAccess(url: String, username: String, password: String) {
    fun getCustomerName(): String = "Max Mustermann"
    fun getAddress(): String = "Musterstraße 1, 12345 Musterstadt"
}

class UserService(url: String, username: String, password: String) {
    private val databaseAccess = DatabaseAccess(url, username, password)
    fun getCustomerByName(name: String) = databaseAccess.getCustomerName()
}

class AddressService(url: String, username: String, password: String) {
    private val databaseAccess = DatabaseAccess(url, username, password)
    fun getAddressByName(name: String) = databaseAccess.getAddress()
}

fun startApplication() {
    val userService = UserService("jdbc:mysql://localhost:3306/mydb", "user", "password")
    val addressService = AddressService("jdbc:mysql://localhost:3306/mydb", "user", "password")

    val user = userService.getCustomerByName("Max Mustermann")
    val address = addressService.getAddressByName("Max Mustermann")
}




// Dependency Injection

class UserServiceDI(private val databaseAccess: DatabaseAccess) {
    fun getCustomerByName(name: String) = databaseAccess.getCustomerName()
}

class AddressServiceDI(private val databaseAccess: DatabaseAccess) {
    fun getAddressByName(name: String) = databaseAccess.getAddress()
}


fun main() {
    val databaseAccess = DatabaseAccess("jdbc:mysql://localhost:3306/mydb", "user", "password")

    val userServiceDI = UserServiceDI(databaseAccess)
    val addressServiceDi = AddressServiceDI(databaseAccess)

    val user = userServiceDI.getCustomerByName("Max Mustermann")
    val address = addressServiceDi.getAddressByName("Max Mustermann")
}







