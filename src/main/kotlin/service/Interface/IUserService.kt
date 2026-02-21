package com.ktor.service.interfaces

import com.fasterxml.jackson.annotation.ObjectIdGenerators
import com.ktor.dto.common.PaginatedResponse
import com.ktor.dto.user.CreateUserRequest
import com.ktor.dto.user.UpdateUserRequest
import com.ktor.dto.user.UserResponse

interface IUserService {

    suspend fun createUser(request: CreateUserRequest): UserResponse
//    suspend fun createUser(request: CreateUserRequest):
    //    UserResponse
suspend fun getUserPaginated (
    page : Int,
    size : Int,
    sortBy: String,
    order : String
): PaginatedResponse<UserResponse>


    suspend fun getAllUsers(): List<UserResponse>
    suspend fun getUserById(id: Int): UserResponse
    suspend fun updateUser(id: Int, request: UpdateUserRequest): UserResponse
    suspend fun deleteUser(id: Int): Boolean
}