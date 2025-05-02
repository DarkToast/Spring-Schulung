package com.qvestdigital.springschulung.author

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
class AuthorValidator : ConstraintValidator<ValidAuthor, AuthorWrite> {
    override fun isValid(value: AuthorWrite?, context: ConstraintValidatorContext?): Boolean {
        return value != null && value.name != value.surname
    }
}

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [AuthorValidator::class])
annotation class ValidAuthor(
    val message: String = "Cross field validation is false",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)