package com.qvestdigital.examples.spring_di

import org.springframework.boot.WebApplicationType
import org.springframework.boot.builder.SpringApplicationBuilder

fun main() {
    val context = SpringApplicationBuilder(Application::class.java)
        .web(WebApplicationType.NONE)
        .run()

    val app = context.getBean(Application::class.java)
    app.userRequest()
}