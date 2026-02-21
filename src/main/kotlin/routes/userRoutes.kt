package com.ktor.routes

import com.ktor.controller.UserController
import com.ktor.plugins.requireRole
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Route.userRoutes(controller: UserController) {

// lo que se pone en authenticate requiere token

    authenticate ("auth-jwt"){
    route("/users") {



       //  POST /users - Crear usuario SOLO ADMIN
        post {
            controller.create(call)
        }

        get {
            controller.findAllPaginated(call)
        }

//        // GET /users - Obtener todos los usuarios
//        get {
//            controller.getAll(call)
//        }

        // GET /users/{id} - Obtener usuario por ID
        get("/{id}") {
            controller.getById(call)
        }

        // PUT /users/{id} - Actualizar usuario
        put("/{id}") {
            controller.update(call)
        }

        // DELETE /users/{id} - Eliminar usuario
        requireRole("ADMIN") {
            delete("/{id}") {

                controller.delete(call)
            }
        }
    }
    }
}