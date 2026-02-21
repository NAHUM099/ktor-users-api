package com.ktor.dto.product
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class CreateProductRequest (
    val name : String,
    val price : Double,
    val stock : Int
)