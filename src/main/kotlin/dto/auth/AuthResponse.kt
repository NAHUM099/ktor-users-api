package com.ktor.dto.auth

import kotlinx.serialization.Serializable


@Serializable
data class AuthResponse (
    val token : String,
    val userId : Int,
    val email : String,
    val name : String,
    val message :String? = null
)
