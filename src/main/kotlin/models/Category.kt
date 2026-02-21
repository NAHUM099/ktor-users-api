package com.ktor.models



data class Category (
    val id : Int,
    val name : String,
    val description : String? = null
)