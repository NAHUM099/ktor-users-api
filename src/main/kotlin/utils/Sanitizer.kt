package com.ktor.utils

object Sanitizer {

    fun sanitize(input: String?): String {
        return input!!
            .trim()
            .replace(Regex("\\s+"), " ")
            .lowercase()
    }

    fun sanitizeLower(input: String): String {
        return input.trim().lowercase()
    }
}