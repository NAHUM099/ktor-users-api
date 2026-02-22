package com.ktor.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.requireRole(requiredRole: String, build: Route.() -> Unit) {

    authenticate("auth-jwt") {

        intercept(ApplicationCallPipeline.Call) {

            val principal = call.principal<JWTPrincipal>()
            val role = principal
                ?.payload
                ?.getClaim("role")
                ?.asString()

            if (role?.uppercase() != requiredRole.uppercase()) {
                call.respond(
                    HttpStatusCode.Forbidden,
                    "No tienes permisos para acceder a este recurso"
                )
                finish()
            }
        }

        build()
    }
}