package com.ktor.service.Interface

import com.ktor.dto.product.CreateProductRequest
import com.ktor.dto.product.ProductResponse
import com.ktor.dto.product.UpdateProductRequest
import com.ktor.models.Product

interface IProductService {

    suspend fun createProduct(request: CreateProductRequest) : ProductResponse
    suspend fun getAllProduct() : List<ProductResponse>
    suspend fun getByIProduct(id : Int) : ProductResponse
    suspend fun getByNameProduct(name : String) : ProductResponse
    suspend fun updateProduct(id: Int, updateProduct : UpdateProductRequest) : ProductResponse
    suspend fun deleteProduct(id : Int) : Boolean
}