package com.ktor.plugins

import com.ktor.auth.JwtConfig
import com.ktor.exceptions.ErrorCode
import com.ktor.exceptions.ErrorResponse
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.path
import io.ktor.server.response.respond

fun Application.configureAuthentication() {
    install(Authentication) {
        jwt("auth-jwt") {
            verifier(JwtConfig.verifier)

//            validate { credential ->
//                println("🔥 Claims: ${credential.payload.claims}")
//                JWTPrincipal(credential.payload)
//            }

            //validar credenciales
            validate { credential ->
                val userId = credential.payload.getClaim("userId").asInt()
                val email = credential.payload.getClaim("email").asString()
                val role = credential.payload.getClaim("role").asString()

                if (userId != null && email != null && role != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }

            challenge { _, _ ->
                call.respond(
                    io.ktor.http.HttpStatusCode.Unauthorized,
                    ErrorResponse.from(
                        status = 401,
                        errorCode = ErrorCode.UNAUTHORIZED,
                        message = "token invalido o expirado",
                        path = call.request.path()
                    )
                )
            }
        }
    }
}