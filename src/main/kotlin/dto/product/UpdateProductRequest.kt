package com.ktor.dto.product
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.math.BigDecimal


@Serializable
data class UpdateProductRequest (
    val name : String? = null,
    val price : Double? = null,
    val stock : Int? = null

)