package com.ktor.plugins

import com.ktor.auth.AuthService
import com.ktor.controller.AuthController
import com.ktor.controller.CategoryController
import com.ktor.controller.ProductController
import com.ktor.controller.UserController
import com.ktor.routes.authRoutes
import com.ktor.routes.categoryRoutes
import com.ktor.routes.productRoute
import com.ktor.routes.userRoutes
import com.ktor.service.CategoryService
import com.ktor.service.ProductService
import com.ktor.service.UserService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import repository.UserRepository
import com.ktor.repository.CategoryRepository
import com.ktor.repository.ProductRepository

fun Application.configureRouting() {

    // Dependency Injection manual
    val userRepository = UserRepository()
    val userService = UserService(userRepository)
    val userController = UserController(userService)

    val productRepository = ProductRepository()
    val productService = ProductService(productRepository)
    val productController = ProductController(productService)

    val categoryRepository = CategoryRepository()
    val categoryService = CategoryService(categoryRepository)
    val categoryController = CategoryController(categoryService)

    // 🆕 Auth
    val authService = AuthService(userRepository)
    val authController = AuthController(authService)

    routing {
        // Health check (pública)
        get("/health") {
            call.respond(mapOf("status" to "OK"))
        }

        //  Rutas de autenticación (públicas)
        authRoutes(authController)

        //  RUTAS PROTEGIDAS (requieren JWT)
        authenticate("auth-jwt") {
            userRoutes(userController)
            productRoute(productController)
            categoryRoutes(categoryController)
        }
    }
}