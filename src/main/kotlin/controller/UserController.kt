package com.ktor.controller

import com.ktor.dto.common.PaginatedResponse
import com.ktor.dto.user.CreateUserRequest
import com.ktor.dto.user.UpdateUserRequest
import com.ktor.dto.user.UserResponse
import com.ktor.service.interfaces.IUserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.*
import io.ktor.server.response.*

class UserController(
    private val userService: IUserService
) {

    // POST /users - Crear usuario
    suspend fun create(call: ApplicationCall) {

        val principal = call.principal<JWTPrincipal>()
        val roleFromToken = principal?.payload?.getClaim("role")?.asString()

        if (roleFromToken?.uppercase() != "ADMIN") {
            call.respond(
                HttpStatusCode.Forbidden,
                mapOf("error" to "Solo ADMIN puede crear usuarios")
            )
            return
        }

        val request = call.receive<CreateUserRequest>()
        val response = userService.createUser(request)

        call.respond(HttpStatusCode.Created, response)
    }
    suspend fun findAllPaginated(call: ApplicationCall) {
        val page = call.request.queryParameters["page"]?.toIntOrNull() ?:1
        val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 10
        val sortBy = call.request.queryParameters["sortBy"]?: "id"
        val order = call.request.queryParameters["order"] ?: "asc"

        val result = userService.getUserPaginated(page, size, sortBy, order)

        call.respond<PaginatedResponse<UserResponse>>(HttpStatusCode.OK, result)
    }
    // GET /users - Obtener todos los usuarios
    suspend fun getAll(call: ApplicationCall) {
        val users = userService.getAllUsers()
        call.respond(HttpStatusCode.OK, users)
    }

    // GET /users/{id} - Obtener usuario por ID
    suspend fun getById(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "ID inválido")
            )

        val user = userService.getUserById(id)
        call.respond(HttpStatusCode.OK, user)
    }

    // PUT /users/{id} - Actualizar usuario
    suspend fun update(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "ID inválido")
            )

        val request = call.receive<UpdateUserRequest>()
        val user = userService.updateUser(id, request)
        call.respond(HttpStatusCode.OK, user)
    }

    // DELETE /users/{id} - Eliminar usuario
    suspend fun delete(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "ID inválido")
            )

        userService.deleteUser(id)
        call.respond(
            HttpStatusCode.OK,
            mapOf("message" to "Usuario eliminado correctamente")
        )
    }
}