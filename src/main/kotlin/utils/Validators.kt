package com.ktor.utils

import com.ktor.exceptions.BadRequestException

object Validators {

    private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

    /**
     * Valida que el nombre sea válido
     * @throws BadRequestException si el nombre es inválido
     */
    fun validateName(name: String) {
        if (name.isBlank()) {
            throw BadRequestException.invalidName()
        }

        if (name.length > 100) {
            throw BadRequestException.fieldTooLong("nombre", 100)
        }
    }

    /**
     * Valida que el email sea válido
     * @throws BadRequestException si el email es inválido
     */
    fun validateEmail(email: String) {
        if (email.isBlank()) {
            throw BadRequestException.fieldRequired("email")
        }

        if (email.length > 150) {
            throw BadRequestException.fieldTooLong("email", 150)
        }

        if (!email.matches(EMAIL_REGEX)) {
            throw BadRequestException.invalidEmail(email)
        }
    }

    /**
     * Valida que el precio sea válido
     * @throws BadRequestException si el precio es inválido
     */
    fun validatePrice(price: Double) {
        if (price <= 0) {
            throw BadRequestException.invalidPrice()
        }

        if (price >= 1_000_000_000) {
            throw BadRequestException.fieldTooLong("precio", 1_000_000_000)
        }
    }

    /**
     * Valida que el stock sea válido
     * @throws BadRequestException si el stock es inválido
     */
    fun validateStock(stock: Int) {
        if (stock < 0) {
            throw BadRequestException.invalidStock()
        }

        if (stock >= 1_000_000) {
            throw BadRequestException.fieldTooLong("stock", 1_000_000)
        }
    }

    /**
     * Valida que el ID sea válido
     * @throws BadRequestException si el ID es inválido
     */
    fun validateId(id: Int) {
        if (id <= 0) {
            throw BadRequestException.invalidId()
        }
    }

    /**
     * Valida que la descripción sea válida
     * @throws BadRequestException si la descripción es inválida
     */
    fun validateDescription(description: String?) {
        description?.let {
            if (it.length > 255) {
                throw BadRequestException.fieldTooLong("descripción", 255)
            }
        }
    }
}