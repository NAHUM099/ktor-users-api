package com.ktor.repository.interfaces

import com.ktor.dto.product.CreateProductRequest
import com.ktor.dto.product.UpdateProductRequest
import com.ktor.models.Product
import java.math.BigDecimal

interface IProductRepository {
    fun create(name: String, price: BigDecimal, stock: Int): Int
    fun findAll(): List<Product>
    fun findById(id: Int): Product?
    fun findByName(name: String): Product?
    fun update(id: Int, name: String?, price: BigDecimal?, stock: Int?): Boolean
    fun delete(id: Int): Boolean
}

