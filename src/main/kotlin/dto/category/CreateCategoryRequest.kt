package com.ktor.dto.category

import kotlinx.serialization.Serializable

@Serializable
 data class CreateCategoryRequest (
    val name : String,
    val description : String? = null
)