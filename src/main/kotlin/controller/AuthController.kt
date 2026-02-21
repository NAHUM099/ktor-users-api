package com.ktor.controller

import com.ktor.auth.AuthService
import com.ktor.dto.auth.LoginRequest
import com.ktor.dto.auth.RegisterRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class AuthController(
    private val authService: AuthService
) {

    /**
     * POST /auth/register
     * Registra un nuevo usuario
     */
    suspend fun register(call: ApplicationCall) {
        val request = call.receive<RegisterRequest>()
        val response = authService.register(request)
        call.respond(HttpStatusCode.Created, response)
    }

    /**
     * POST /auth/login
     * Inicia sesión y retorna un JWT token
     */
    suspend fun login(call: ApplicationCall) {
        val request = call.receive<LoginRequest>()
        val response = authService.login(request)
        call.respond(HttpStatusCode.OK, response)
    }
}