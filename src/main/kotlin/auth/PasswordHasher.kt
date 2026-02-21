package com.ktor.auth

import org.mindrot.jbcrypt.BCrypt


object PasswordHasher {

    fun hashPassword (password : String) : String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun verifyPassword (password: String, hashPassword : String) : Boolean {
        return try{
            BCrypt.checkpw(password, hashPassword)
        } catch (e : Exception) {
            false
        }
    }
}