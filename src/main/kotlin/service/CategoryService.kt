package com.ktor.service

import com.ktor.dto.category.CategoryResponse
import com.ktor.dto.category.CreateCategoryRequest
import com.ktor.dto.category.UpdateCategoryRequest
import com.ktor.exceptions.ConflictException
import com.ktor.exceptions.NotFoundException
import com.ktor.models.Category
import com.ktor.repository.CategoryRepository
import com.ktor.service.Interface.ICategoryService
import com.ktor.utils.Sanitizer
import com.ktor.utils.Validators

class CategoryService(
    private val repositoryCategory: CategoryRepository
) : ICategoryService {

    override suspend fun createCategory(request: CreateCategoryRequest): CategoryResponse {

        val name = Sanitizer.sanitize(request.name)
        val description = Sanitizer.sanitize(request.description)

        // 1. Validar datos
        Validators.validateName(request.name)
        Validators.validateDescription(request.description)

        // 2. Verificar que no exista
        val existe = repositoryCategory.findByName(request.name)
        if (existe != null) {
            throw ConflictException.categoryExists(request.name)
        }

        // 3. Crear
        val categoryId = repositoryCategory.create(request.name, request.description)

        // 4. Retornar
        return CategoryResponse(
            id = categoryId,
            name = name,
            description = description
        )
    }

    override suspend fun getAllCategories(): List<CategoryResponse> {
        val categories = repositoryCategory.findAll()
        return categories.map { it.toResponse() }
    }

    override suspend fun getByIdCategory(id: Int): CategoryResponse {
        Validators.validateId(id)

        val category = repositoryCategory.findById(id)
            ?: throw NotFoundException.category(id)

        return category.toResponse()
    }

    override suspend fun updateCategory(id: Int, request: UpdateCategoryRequest): CategoryResponse {
        // 1. Verificar que exista
        repositoryCategory.findById(id)
            ?: throw NotFoundException.category(id)

        // 2. Validar nuevos datos
        request.name?.let { Validators.validateName(it) }
        request.description?.let { Validators.validateDescription(it) }

        // 3. Verificar nombre duplicado
        request.name?.let { newName ->
            val categoryWithName = repositoryCategory.findByName(newName)
            if (categoryWithName != null && categoryWithName.id != id) {
                throw ConflictException.categoryExists(newName)
            }
        }

        // 4. Actualizar
        val updated = repositoryCategory.update(id, request.name, request.description)
        if (!updated) {
            throw NotFoundException.category(id)
        }

        // 5. Obtener actualizada
        val updatedCategory = repositoryCategory.findById(id)
            ?: throw NotFoundException.category(id)

        return updatedCategory.toResponse()
    }

    override suspend fun deleteCategory(id: Int): Boolean {
        Validators.validateId(id)

        val deleted = repositoryCategory.delete(id)
        if (!deleted) {
            throw NotFoundException.category(id)
        }

        return true
    }

    // Extension function
    private fun Category.toResponse(): CategoryResponse {
        return CategoryResponse(
            id = this.id,
            name = this.name,
            description = this.description
        )
    }
}