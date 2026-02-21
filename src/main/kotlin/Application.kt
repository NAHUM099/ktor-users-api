package com.ktor

import com.ktor.auth.PasswordHasher
import com.ktor.plugins.configureAuthentication
import com.ktor.plugins.configureLogging
import com.ktor.plugins.configureRouting
import com.ktor.plugins.configureSerialization
import com.ktor.plugins.configureStatusPages
import com.ktor.plugins.configureCORS
import config.DatabaseFactory
import io.ktor.server.application.*


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    // 1. Configurar plugins
    configureLogging()
    configureCORS()
    configureSerialization()
    configureStatusPages()
    configureAuthentication()

    // 2. Inicializar base de datos
    DatabaseFactory.init()

    // 3. Configurar rutas
    configureRouting()

    println(" Servidor iniciado correctamente")
    println(" Health check: http://localhost:8080/health")
    println(" Auth API: http://localhost:8080/auth")
    println(" Users API: http://localhost:8080/users")

//    val hash = PasswordHasher.hashPassword("123456")
//    println(hash)
}