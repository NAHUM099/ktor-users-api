package com.ktor.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.github.cdimascio.dotenv.dotenv
import java.util.*

object JwtConfig {

    private val dotenv = dotenv()

    // Configuración del JWT
    private val secret = dotenv["JWT_SECRET"]
        ?: System.getenv("JWT_SECRET")
        ?:throw error("JWT_SECRET no esta configurado")
    private val issuer = dotenv["JWT_ISSUER"]
        ?: System.getenv("JWT_ISSUER")
        ?:throw error("JWT_ISSUER no esta configurado")
    private val audience = dotenv["JWT_AUDIENCE"]
        ?: System.getenv("JWT_AUDIENCE")
        ?:throw error("JWT_AUDIENCE no esta configurado")
    private val validityInMs = 36_000_00 * 10 // 10 horas en milisegundos

    private val algorithm = Algorithm.HMAC256(secret)

    /**
     * Verificador de JWT
     */
    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .withAudience(audience)
        .build()

    /**
     * Genera un token JWT para un usuario
     */
    fun generateToken(userId: Int, email: String, role: String): String {
        return JWT.create()
            .withSubject("Authentication")
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("userId", userId)
            .withClaim("email", email)
            .withClaim("role", role)
            .withExpiresAt(Date(System.currentTimeMillis() + validityInMs))
            .sign(algorithm)
    }

    /**
     * Extrae el userId del token
     */
    fun getUserId(token: String): Int? {
        return try {
            val decodedJWT = verifier.verify(token)
            decodedJWT.getClaim("userId").asInt()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Extrae el email del token
     */
    fun getEmail(token: String): String? {
        return try {
            val decodedJWT = verifier.verify(token)
            decodedJWT.getClaim("email").asString()
        } catch (e: Exception) {
            null
        }
    }
}