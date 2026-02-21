package com.ktor.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureCORS() {
    install(CORS) {
        // Permitir solicitudes desde estos orígenes

        allowHost("localhost:3000")  // Frontend local
        allowHost("192.168.1.10:3000")// Frontend en red local
        allowHost("myapp.com", schemes = listOf("https"))  // Producción

        // Métodos HTTP permitidos

        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Options)

        // Headers permitidos

        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)

        // Permitir credenciales (cookies, auth headers)

        allowCredentials = true

        // Tiempo que el navegador cachea la respuesta CORS

        maxAgeInSeconds = 3600
    }
}