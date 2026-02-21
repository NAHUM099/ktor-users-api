package com.ktor.routes

import com.ktor.controller.CategoryController
import io.ktor.server.routing.*




fun Route.categoryRoutes (
     controllerCategory : CategoryController
){

    route ("/categories") {

        post {
            controllerCategory.create(call)
        }

        get {
            controllerCategory.getAll(call)
        }
        get ("/{id}") {
            controllerCategory.getById(call)
        }
        put ("/{id}"){
            controllerCategory.update(call)
        }
        delete ("/{id}") {
            controllerCategory.delete(call)
        }


    }
}