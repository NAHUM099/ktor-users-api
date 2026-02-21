package com.ktor.service.Interface

import com.ktor.dto.category.CategoryResponse
import com.ktor.dto.category.CreateCategoryRequest
import com.ktor.dto.category.UpdateCategoryRequest

interface ICategoryService {

    suspend fun createCategory (request: CreateCategoryRequest) : CategoryResponse
    suspend fun getAllCategories() : List<CategoryResponse>
    suspend fun getByIdCategory(id : Int) : CategoryResponse
    suspend fun updateCategory (id : Int, request: UpdateCategoryRequest) : CategoryResponse
    suspend fun deleteCategory(id  :Int) : Boolean
}