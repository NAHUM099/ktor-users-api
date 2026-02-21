package com.ktor.plugins

import com.ktor.exceptions.*
import io.github.cdimascio.dotenv.dotenv
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.sql.SQLException


fun Application.configureStatusPages() {
    install(StatusPages) {

        // ============= MANEJO DE EXCEPCIONES PERSONALIZADAS =============

        exception<AppException> { call, cause ->
            val errorResponse = ErrorResponse.from(
                status = cause.statusCode.value,
                errorCode = cause.errorCode,
                message = cause.message,
                path = call.request.path(),
                details = cause.details
            )

            call.respond(cause.statusCode, errorResponse)
            logError(call, cause)
        }

        // ============= MANEJO DE ERRORES DE BASE DE DATOS =============

        exception<ExposedSQLException> { call, cause ->
            val errorResponse = when {
                // Violación de clave única (duplicate key)
                cause.message?.contains("unique constraint", ignoreCase = true) == true ||
                        cause.message?.contains("duplicate key", ignoreCase = true) == true -> {
                    ErrorResponse.from(
                        status = HttpStatusCode.Conflict.value,
                        errorCode = ErrorCode.DUPLICATE_KEY,
                        message = "Ya existe un registro con estos datos",
                        path = call.request.path(),
                        details = mapOf("sqlError" to (cause.message ?: "Unknown SQL error"))
                    )
                }

                // Violación de foreign key
                cause.message?.contains("foreign key", ignoreCase = true) == true -> {
                    ErrorResponse.from(
                        status = HttpStatusCode.BadRequest.value,
                        errorCode = ErrorCode.FOREIGN_KEY_VIOLATION,
                        message = "No se puede eliminar este recurso porque está siendo usado",
                        path = call.request.path()
                    )
                }

                // Otros errores de BD
                else -> {
                    ErrorResponse.from(
                        status = HttpStatusCode.InternalServerError.value,
                        errorCode = ErrorCode.DATABASE_ERROR,
                        message = "Error de base de datos",
                        path = call.request.path()
                    )
                }
            }

            call.respond(HttpStatusCode.fromValue(errorResponse.status), errorResponse)
            logError(call, cause)
        }

        exception<SQLException> { call, cause ->
            val errorResponse = ErrorResponse.from(
                status = HttpStatusCode.InternalServerError.value,
                errorCode = ErrorCode.DATABASE_ERROR,
                message = "Error de base de datos",
                path = call.request.path()
            )

            call.respond(HttpStatusCode.InternalServerError, errorResponse)
            logError(call, cause)
        }

        // ============= MANEJO DE ERRORES DE VALIDACIÓN =============

        exception<IllegalArgumentException> { call, cause ->
            val errorResponse = ErrorResponse.from(
                status = HttpStatusCode.BadRequest.value,
                errorCode = ErrorCode.VALIDATION_ERROR,
                message = cause.message ?: "Error de validación",
                path = call.request.path()
            )

            call.respond(HttpStatusCode.BadRequest, errorResponse)
            logError(call, cause)
        }

        // ============= MANEJO DE ERRORES GENÉRICOS =============

        exception<Throwable> { call, cause ->
            val errorResponse = ErrorResponse.from(
                status = HttpStatusCode.InternalServerError.value,
                errorCode = ErrorCode.INTERNAL_SERVER_ERROR,
                message = "Error interno del servidor",
                path = call.request.path()
            )

            call.respond(HttpStatusCode.InternalServerError, errorResponse)
            logError(call, cause)
        }
    }
}

// ============= FUNCIÓN DE LOGGING =============

private fun logError(call: ApplicationCall, cause: Throwable) {
    val method = call.request.httpMethod.value
    val path = call.request.path()
    val errorType = cause::class.simpleName

    println(" ERROR [$errorType] $method $path - ${cause.message}")

    // En producción, aquí usarías un logger real como Logback
    // log.error("Error en $method $path", cause)

    // Solo en desarrollo, imprime el stacktrace completo
    if (isDevelopment()) {
        cause.printStackTrace()
    }
}


private val dotenv = dotenv()
private fun isDevelopment(): Boolean {

    val environment = dotenv["ENVIRONMENT"]
        ?: System.getenv("ENVIROMENT")
        ?: "development"

    return environment != "production"
}