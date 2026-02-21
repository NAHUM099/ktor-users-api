package com.ktor.repository.interfaces

import com.ktor.dto.user.CreateUserRequest
import com.ktor.dto.user.UserResponse
import models.User

interface IUserRepository {

    fun findAllPaginated(
        page: Int,
        size : Int,
        sortBy: String,
        order: String
    ) : Pair<List<User>, Long>



    fun create (name : String, email : String, password : String, role : String) : Int

    fun findAll(): List<User>
    fun findById(Id: Int) : User?
    fun findByEmail (email: String) : User?
    fun update (id: Int, name :String?, email: String?) : Boolean
    fun delete(id : Int) : Boolean
}