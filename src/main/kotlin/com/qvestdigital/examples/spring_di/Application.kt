package com.qvestdigital.examples.spring_di

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan

@ComponentScan("com.qvestdigital.examples.spring_di")
@EnableConfigurationProperties(DatabaseProperties::class, ExternalServiceConfig::class)
class Application(val userService: UserService, val addressService: AddressService) {
    fun userRequest() {
        val user = userService.getCustomerByName("Max Mustermann")
        val address = addressService.getAddressByName("Mustermann")

        println("====================================================================")
        println("User: $user with address: $address")
        println("====================================================================")
    }
}