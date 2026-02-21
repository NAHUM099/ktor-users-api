package com.ktor.controller

import com.ktor.dto.category.CreateCategoryRequest
import com.ktor.dto.category.UpdateCategoryRequest
import com.ktor.service.Interface.ICategoryService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond

class CategoryController (
    private val serviceCategory : ICategoryService
) {


    suspend fun create (call : ApplicationCall) {
        val category = call.receive<CreateCategoryRequest>()
        val response = serviceCategory.createCategory(category)

        call.respond(HttpStatusCode.Created, response)
    }

    suspend fun getAll(call : ApplicationCall) {
        val response = serviceCategory.getAllCategories()

        call.respond(HttpStatusCode.OK, response)
    }

    suspend fun getById (call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "ID invalido")
            )
        val response = serviceCategory.getByIdCategory(id)

        call.respond(HttpStatusCode.OK, response )
    }


    suspend fun update (call : ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "ID invalido")
            )

        val request = call.receive<UpdateCategoryRequest>()
        val response  = serviceCategory.updateCategory(id, request)

        call.respond(HttpStatusCode.OK, response)
    }


    suspend fun delete (call : ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "ID invalido")
            )

        serviceCategory.deleteCategory(id)

        call.respond(HttpStatusCode.OK,
            mapOf("message" to "categoria eliminada exitosamente"))
    }
}