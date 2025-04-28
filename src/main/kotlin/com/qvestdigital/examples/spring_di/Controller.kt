package com.qvestdigital.examples.spring_di

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

data class Customer(
    val id: Long,
    val name: String,
    val address: String
)


@RestController
@RequestMapping("/customer")
class Controller(val userService: UserService) {

    var username: String = ""
    var password: String = ""

    @GetMapping("/{id}")
    fun findCustomer(@PathVariable id: Int): ResponseEntity<Customer?> {
        return if (id == 1) {
            val c = Customer(1, "Max Mustermann", "Musterstraße 1")
            ResponseEntity.ok(c)
        } else {
            ResponseEntity.notFound().build<Customer>()
        }
    }

    @PostMapping("/")
    fun addCustomer(customer: Customer): ResponseEntity<*> {
        println(customer)
        return ResponseEntity.status(201).build<Any>()
    }

    @GetMapping("/{id}")
    fun read(@PathVariable id: Long): ResponseEntity<Customer?> {
        val customer = userService.findById(id)
        return if (customer == null) {
            ResponseEntity.notFound().build()
        } else {
            ResponseEntity.ok(customer)
        }
    }

    @GetMapping("/")
    fun find(@RequestParam name: String?): ResponseEntity<Customer?> {
        val customer = userService.findByName(name ?: "")
        return if (customer == null) {
            ResponseEntity.notFound().build()
        } else {
            ResponseEntity.ok(customer)
        }
    }
}