package com.ktor.auth

import com.ktor.dto.auth.AuthResponse
import com.ktor.dto.auth.LoginRequest
import com.ktor.dto.auth.RegisterRequest
import com.ktor.exceptions.BadRequestException
import com.ktor.exceptions.ConflictException
import com.ktor.exceptions.ErrorCode
import com.ktor.exceptions.NotFoundException
import com.ktor.repository.interfaces.IUserRepository
import com.ktor.utils.Sanitizer
import com.ktor.utils.Validators
import org.slf4j.LoggerFactory

class AuthService(
    private val userRepository: IUserRepository
) {

    private val logger = LoggerFactory.getLogger(AuthService::class.java)

    /**
     * Registra un nuevo usuario
     */

    suspend fun register(request: RegisterRequest): AuthResponse {


        val email = Sanitizer.sanitizeLower(request.email)
        val name = Sanitizer.sanitize(request.name)

        logger.info("Intento de registro para el email: ${email}")
        // 1. Validar datos
        Validators.validateName(name)
        Validators.validateEmail(email)
        validatePassword(request.password)

        // 2. Verificar que el email no exista
        val existingUser = userRepository.findByEmail(email)
        if (existingUser != null) {
            logger.warn("Intento de registro con email ya existente: ${email}")
            throw ConflictException.userExists(email)
        }

        // 3. Hashear la contraseña
        val hashedPassword = PasswordHasher.hashPassword(request.password)

        // 4. Crear usuario
        val userId = userRepository.create(
            name = name,
            email = email,
            password = hashedPassword,
            role = "USER"
        )
        logger.info("Usuario registrado exitosamente con ID $userId y email ${email}")


        // 5. Generar token
        val token = JwtConfig.generateToken(userId, email, "USER")

        // 6. Retornar respuesta
        return AuthResponse(
            token = token,
            userId = userId,
            email = email,
            name = name,
            message = "Usuario registrado exitosamente"
        )
    }

    /**
     * Inicia sesión de un usuario
     */
    suspend fun login(request: LoginRequest): AuthResponse {

        val email = Sanitizer.sanitizeLower(request.email)
        logger.info("Intento de login para el email: ${email}")

// 1. Validar datos

        Validators.validateEmail(email)
        if (request.password.isBlank()) {
            logger.warn("Login fallido: contraseña vacía para el email ${email}")
            throw BadRequestException(
                errorCode = ErrorCode.FIELD_REQUIRED,
                message = "La contraseña es requerida"
            )
        }

        // 2. Buscar usuario por email
        val user = userRepository.findByEmail(request.email)
            ?:run {
                logger.warn("Login fallido: email no encontrado (${email})")
                throw NotFoundException(
                errorCode = ErrorCode.USER_NOT_FOUND,
                message = "Credenciales inválidas"
            )
            }

        // 3. Verificar contraseña
        val passwordMatches = PasswordHasher.verifyPassword(
            password = request.password,
            hashPassword = user.password ?: ""
        )

        if (!passwordMatches) {
            logger.warn("Login fallido: contraseña incorrecta para el email ${email}")
            throw NotFoundException(
                errorCode = ErrorCode.USER_NOT_FOUND,
                message = "Credenciales inválidas"
            )
        }

        logger.info("Login exitoso para el usuario con ID ${user.id}")


        // 4. Generar token
        val token = JwtConfig.generateToken(user.id, user.email, user.role)

        // 5. Retornar respuesta
        return AuthResponse(
            token = token,
            userId = user.id,
            email = email,
            name = user.name,
            message = "Login exitoso"
        )
    }

    /**
     * Valida que la contraseña cumpla requisitos mínimos
     */
    private fun validatePassword(password: String) {
        if (password.isBlank()) {
            throw BadRequestException(
                errorCode = ErrorCode.FIELD_REQUIRED,
                message = "La contraseña es requerida"
            )
        }

        if (password.length < 6) {
            throw BadRequestException(
                errorCode = ErrorCode.FIELD_TOO_SHORT,
                message = "La contraseña debe tener al menos 6 caracteres"
            )
        }

        if (password.length > 100) {
            throw BadRequestException(
                errorCode = ErrorCode.FIELD_TOO_LONG,
                message = "La contraseña no puede tener más de 100 caracteres"
            )
        }
    }
}