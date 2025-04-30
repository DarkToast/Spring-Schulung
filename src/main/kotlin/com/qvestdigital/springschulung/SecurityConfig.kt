package com.qvestdigital.springschulung

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableMethodSecurity
class SecurityConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { auth -> auth.anyRequest().authenticated() }
            .httpBasic { }
        return http.build()
    }

    @Bean
    fun inMemoryUserDetailsManager(passwordEncoder: PasswordEncoder): InMemoryUserDetailsManager {
        val user1 = org.springframework.security.core.userdetails.User
            .withUsername("reader")
            .password(passwordEncoder.encode("password123"))
            .roles("READ")
            .build()

        val user2 = org.springframework.security.core.userdetails.User
            .withUsername("writer")
            .password(passwordEncoder.encode("password456"))
            .roles("WRITE", "READ")
            .build()

        return InMemoryUserDetailsManager(user1, user2)
    }
}