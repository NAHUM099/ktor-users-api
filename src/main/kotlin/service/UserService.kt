package com.ktor.service

import com.ktor.auth.PasswordHasher
import com.ktor.dto.common.PaginatedResponse
import com.ktor.dto.user.CreateUserRequest
import com.ktor.dto.user.UpdateUserRequest
import com.ktor.dto.user.UserResponse
import com.ktor.exceptions.ConflictException
import com.ktor.exceptions.ErrorCode
import com.ktor.exceptions.NotFoundException
import com.ktor.repository.interfaces.IUserRepository
import com.ktor.service.interfaces.IUserService
import com.ktor.utils.Sanitizer
import com.ktor.utils.Validators
import models.User
import org.slf4j.LoggerFactory

class UserService(
    private val repository: IUserRepository
) : IUserService {
    private val logger = LoggerFactory.getLogger(UserService::class.java)

    override suspend fun createUser(request: CreateUserRequest): UserResponse {

        val name = Sanitizer.sanitize(request.name)
        val email = Sanitizer.sanitizeLower(request.email)

        Validators.validateName(name)
        Validators.validateEmail(email)

        if (request.password.length < 6) {
            throw ConflictException(ErrorCode.INVALID_CREDENTIALS,"la contraseña debe tener almenos 6 caracteres")
        }

        val existingUser = repository.findByEmail(email)
        if (existingUser != null) {
            throw ConflictException.userExists(email)
        }

        val hashedPassword = PasswordHasher.hashPassword(request.password)

        val validRole =
            if (request.role.uppercase() == "ADMIN") "ADMIN"
            else "USER"

        val userId = repository.create(
            name = name,
            email = email,
            password = hashedPassword,
            role = validRole
        )

        return UserResponse(
            id = userId,
            name = name,
            email = email
        )
    }

//    override suspend fun createUser(request: CreateUserRequest): UserResponse {
//        // 1. Validar datos de entrada
//        Validators.validateName(request.name)
//        Validators.validateEmail(request.email)
//
//        // 2. Verificar que el email no exista
//        val existingUser = repository.findByEmail(request.email)
//        if (existingUser != null) {
//            throw ConflictException.userExists(request.email)
//        }
//
//        // 3. Crear usuario en BD
//        val userId = repository.create(request.name, request.email)
//
//        // 4. Retornar respuesta
//        return UserResponse(
//            id = userId,
//            name = request.name,
//            email = request.email
//        )
//    }

    override suspend fun getUserPaginated(page: Int, size: Int, sortBy: String, order : String): PaginatedResponse<UserResponse> {
        val (users, total) = repository.findAllPaginated(page, size, sortBy, order)

        val totalPages = kotlin.math.ceil(total.toDouble() / size).toInt()

        return PaginatedResponse(
            data = users.map{it.toResponse()},
            page = page,
            size = size,
            totalElements = total,
            totalPages = totalPages
        )
    }



    override suspend fun getAllUsers(): List<UserResponse> {
        logger.info("Solicitud para obtener todos los usuarios")

        val users = repository.findAll()
        return users.map { it.toResponse() }

    }

    override suspend fun getUserById(id: Int): UserResponse {
        Validators.validateId(id)

        logger.info("Solicitud para obtener usuario con ID $id")

        val user = repository.findById(id)
            ?: run {
                logger.warn("Usuario con ID $id no encontrado")
                throw NotFoundException.user(id)
            }

        return user.toResponse()
    }

    override suspend fun updateUser(id: Int, request: UpdateUserRequest): UserResponse {
        logger.info("Solicitud para actualizar usuario con ID $id")
        // 1. Verificar que el usuario exista
        repository.findById(id)
            ?: run {
                logger.warn("Intento de actualizar usuario inexistente con ID $id")
                throw NotFoundException.user(id)
            }

        // 2. Validar datos opcionales
        val sanitizedName =  request.name?.let {
            val sanitized = Sanitizer.sanitize(it)
            Validators.validateName(sanitized)
            sanitized
        }

         val sanitizedEmail = request.email?.let {
             val clean = Sanitizer.sanitizeLower(it)
            Validators.validateEmail(clean)

            // Verificar que el nuevo email no esté en uso por otro usuario
            val userWithEmail = repository.findByEmail(clean)
            if (userWithEmail != null && userWithEmail.id != id) {
                logger.warn("Conflicto de email al actualizar usuario: $clean")
                throw ConflictException.userExists(clean)
            }
             clean
        }

        // 3. Actualizar
        val updated = repository.update(
            id = id,
            name = sanitizedName,
            email = sanitizedEmail
        )


        if (!updated) {
            logger.error("Fallo al actualizar usuario con ID $id")
            throw NotFoundException.user(id)
        }

        // 4. Obtener usuario actualizado
        val updatedUser = repository.findById(id)
            ?: throw NotFoundException.user(id)

        logger.info("Usuario con ID $id actualizado correctamente")


        return updatedUser.toResponse()
    }

    override suspend fun deleteUser(id: Int): Boolean {
        Validators.validateId(id)

        logger.info("Solicitud para eliminar usuario con ID $id")

        val deleted = repository.delete(id)
        if (!deleted) {
            throw NotFoundException.user(id)
        }
        logger.info("Usuario con ID $id eliminado correctamente")

        return true
    }

    // Extension function
    private fun User.toResponse(): UserResponse {
        return UserResponse(
            id = this.id,
            name = this.name,
            email = this.email
        )
    }
}