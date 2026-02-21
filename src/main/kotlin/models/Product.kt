package com.ktor.models

import java.math.BigDecimal

data class Product (
    val id : Int,
    val name : String,
    val price : BigDecimal,
    val stock : Int
)