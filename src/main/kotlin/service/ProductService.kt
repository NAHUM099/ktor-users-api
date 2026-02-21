package com.ktor.service

import com.ktor.dto.product.CreateProductRequest
import com.ktor.dto.product.ProductResponse
import com.ktor.dto.product.UpdateProductRequest
import com.ktor.exceptions.ConflictException
import com.ktor.exceptions.NotFoundException
import com.ktor.models.Product
import com.ktor.repository.interfaces.IProductRepository
import com.ktor.service.Interface.IProductService
import com.ktor.utils.Sanitizer
import com.ktor.utils.Validators

class ProductService(
    private val repository: IProductRepository
) : IProductService {

    override suspend fun createProduct(request: CreateProductRequest): ProductResponse {
        // 1. Validar datos
        val name = Sanitizer.sanitize(request.name)

        Validators.validateName(request.name)
        Validators.validatePrice(request.price)
        Validators.validateStock(request.stock)

        // 2. Verificar que el nombre no exista
        val existingProduct = repository.findByName(request.name)
        if (existingProduct != null) {
            throw ConflictException.productExists(request.name)
        }

        // 3. Crear producto (convertir Double → BigDecimal)
        val productId = repository.create(
            name = request.name,
            price = request.price.toBigDecimal(),
            stock = request.stock
        )

        // 4. Retornar respuesta
        return ProductResponse(
            id = productId,
            name = name,
            price = request.price,
            stock = request.stock
        )
    }

    override suspend fun getAllProduct(): List<ProductResponse> {
        val products = repository.findAll()
        return products.map { it.toResponse() }
    }

    override suspend fun getByIProduct(id: Int): ProductResponse {
        Validators.validateId(id)

        val product = repository.findById(id)
            ?: throw NotFoundException.product(id)

        return product.toResponse()
    }

    override suspend fun getByNameProduct(name: String): ProductResponse {
        if (name.isBlank()) {
            throw NotFoundException.resource("Producto")
        }

        val product = repository.findByName(name)
            ?: throw NotFoundException.resource("Producto '$name'")

        return product.toResponse()
    }

    override suspend fun updateProduct(id: Int, updateProduct: UpdateProductRequest): ProductResponse {
        // 1. Verificar que el producto exista
        repository.findById(id)
            ?: throw NotFoundException.product(id)

        // 2. Validar datos opcionales
        updateProduct.name?.let {
            val sanitized = Sanitizer.sanitize(it)
            Validators.validateName(sanitized) }
        updateProduct.price?.let { Validators.validatePrice(it) }
        updateProduct.stock?.let { Validators.validateStock(it) }

        // 3. Verificar nombre duplicado si se está cambiando
        updateProduct.name?.let { newName ->
            val productWithName = repository.findByName(newName)
            if (productWithName != null && productWithName.id != id) {
                throw ConflictException.productExists(newName)
            }
        }

        // 4. Actualizar (convertir Double → BigDecimal si existe)
        val updated = repository.update(
            id = id,
            name = updateProduct.name,
            price = updateProduct.price?.toBigDecimal(),
            stock = updateProduct.stock
        )

        if (!updated) {
            throw NotFoundException.product(id)
        }

        // 5. Retornar producto actualizado
        val updatedProduct = repository.findById(id)
            ?: throw NotFoundException.product(id)

        return updatedProduct.toResponse()
    }

    override suspend fun deleteProduct(id: Int): Boolean {
        Validators.validateId(id)

        val deleted = repository.delete(id)
        if (!deleted) {
            throw NotFoundException.product(id)
        }

        return true
    }

    // Extension function (convertir BigDecimal → Double)
    private fun Product.toResponse(): ProductResponse {
        return ProductResponse(
            id = this.id,
            name = this.name,
            price = this.price.toDouble(),
            stock = this.stock
        )
    }
}