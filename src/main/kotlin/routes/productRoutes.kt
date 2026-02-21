package com.ktor.routes

import com.ktor.controller.ProductController
import io.ktor.server.routing.*



fun Route.productRoute ( productController : ProductController){

    route("/products"){

        post{
            productController.create(call)
        }
        get {
            productController.getAll(call)
        }
        get("/{id}") {
            productController.getById(call)
        }
        put("/{id}"){
            productController.update(call)
        }
        delete ("/{id}"){
            productController.delete(call)
        }

    }

}