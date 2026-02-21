package models

data class User (
    val id : Int,
    val name : String,
    val email: String,
    val password : String? = null,
    val role: String = "USER"
)