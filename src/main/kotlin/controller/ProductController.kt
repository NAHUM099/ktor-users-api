package com.ktor.controller

import com.ktor.dto.product.CreateProductRequest
import com.ktor.dto.product.UpdateProductRequest
import com.ktor.service.ProductService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond


class ProductController (
    val  productService: ProductService
) {

    suspend fun create (call: ApplicationCall) {
        val request = call.receive<CreateProductRequest>()
       val response =  productService.createProduct(request)
        call.respond(HttpStatusCode.Created, response)
    }

    suspend fun getAll (call : ApplicationCall) {
        val products = productService.getAllProduct()
        call.respond(HttpStatusCode.OK, products)
    }

    suspend fun getById (call : ApplicationCall) {
        val byId = call.parameters["id"]?.toIntOrNull()
            ?: return call.respond(
            HttpStatusCode.BadRequest,
            mapOf("error" to "ID invalido"))

        val productId = productService.getByIProduct(byId)
        call.respond(HttpStatusCode.OK, productId)
    }


    suspend fun update (call : ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return call.respond(HttpStatusCode.BadRequest,
            mapOf("error" to "ID invalido"))

        val productUpdate = call.receive<UpdateProductRequest>()
        val update = productService.updateProduct(id, productUpdate)

        call.respond(HttpStatusCode.OK, update)

    }

    suspend fun delete (call : ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to " ID invalido")
            )
         productService.deleteProduct(id)

        call.respond(HttpStatusCode.OK,
            mapOf("message" to "producto eliminado correctamente")
        )

    }
}