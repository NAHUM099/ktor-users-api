package com.ktor.exceptions

import io.ktor.http.*

/**
 * Excepción base para todas las excepciones de la aplicación
 */
sealed class AppException(
    val statusCode: HttpStatusCode,
    val errorCode: ErrorCode,
    override val message: String,
    val details: Map<String, String>? = null
) : Exception(message)

// ============= ERRORES 404 (NOT FOUND) =============

class NotFoundException(
    errorCode: ErrorCode,
    message: String,
    details: Map<String, String>? = null
) : AppException(HttpStatusCode.NotFound, errorCode, message, details) {

    companion object {
        fun user(userId: Int) = NotFoundException(
            errorCode = ErrorCode.USER_NOT_FOUND,
            message = "Usuario con ID $userId no encontrado"
        )

        fun product(productId: Int) = NotFoundException(
            errorCode = ErrorCode.PRODUCT_NOT_FOUND,
            message = "Producto con ID $productId no encontrado"
        )

        fun category(categoryId: Int) = NotFoundException(
            errorCode = ErrorCode.CATEGORY_NOT_FOUND,
            message = "Categoría con ID $categoryId no encontrada"
        )

        fun resource(resourceName: String) = NotFoundException(
            errorCode = ErrorCode.RESOURCE_NOT_FOUND,
            message = "$resourceName no encontrado"
        )
    }
}

// ============= ERRORES 400 (BAD REQUEST) =============

class BadRequestException(
    errorCode: ErrorCode,
    message: String,
    details: Map<String, String>? = null
) : AppException(HttpStatusCode.BadRequest, errorCode, message, details) {

    companion object {
        fun invalidId() = BadRequestException(
            errorCode = ErrorCode.INVALID_ID,
            message = "El ID proporcionado es inválido"
        )

        fun invalidEmail(email: String) = BadRequestException(
            errorCode = ErrorCode.INVALID_EMAIL,
            message = "El email '$email' no es válido"
        )

        fun invalidName() = BadRequestException(
            errorCode = ErrorCode.INVALID_NAME,
            message = "El nombre no puede estar vacío"
        )

        fun invalidPrice() = BadRequestException(
            errorCode = ErrorCode.INVALID_PRICE,
            message = "El precio debe ser mayor a 0"
        )

        fun invalidStock() = BadRequestException(
            errorCode = ErrorCode.INVALID_STOCK,
            message = "El stock no puede ser negativo"
        )

        fun fieldRequired(fieldName: String) = BadRequestException(
            errorCode = ErrorCode.FIELD_REQUIRED,
            message = "El campo '$fieldName' es requerido"
        )

        fun fieldTooLong(fieldName: String, maxLength: Int) = BadRequestException(
            errorCode = ErrorCode.FIELD_TOO_LONG,
            message = "El campo '$fieldName' no puede tener más de $maxLength caracteres"
        )
    }
}

// ============= ERRORES 409 (CONFLICT) =============

class ConflictException(
    errorCode: ErrorCode,
    message: String,
    details: Map<String, String>? = null
) : AppException(HttpStatusCode.Conflict, errorCode, message, details) {

    companion object {
        fun userExists(email: String) = ConflictException(
            errorCode = ErrorCode.EMAIL_ALREADY_EXISTS,
            message = "Ya existe un usuario con el email '$email'"
        )

        fun productExists(name: String) = ConflictException(
            errorCode = ErrorCode.PRODUCT_ALREADY_EXISTS,
            message = "Ya existe un producto con el nombre '$name'"
        )

        fun categoryExists(name: String) = ConflictException(
            errorCode = ErrorCode.CATEGORY_ALREADY_EXISTS,
            message = "Ya existe una categoría con el nombre '$name'"
        )
    }
}

// ============= ERRORES 401 (UNAUTHORIZED) =============

class UnauthorizedException(
    errorCode: ErrorCode = ErrorCode.UNAUTHORIZED,
    message: String = "No autorizado",
    details: Map<String, String>? = null
) : AppException(HttpStatusCode.Unauthorized, errorCode, message, details)

// ============= ERRORES 500 (INTERNAL SERVER ERROR) =============

class DatabaseException(
    errorCode: ErrorCode = ErrorCode.DATABASE_ERROR,
    message: String,
    details: Map<String, String>? = null
) : AppException(HttpStatusCode.InternalServerError, errorCode, message, details)