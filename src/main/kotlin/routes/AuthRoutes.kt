package com.ktor.routes

import com.ktor.controller.AuthController
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.authRoutes(controller: AuthController) {
    route("/auth") {

        // POST /auth/register
        post("/register") {
            controller.register(call)
        }

        // POST /auth/login
        post("/login") {
            controller.login(call)
        }
    }
}