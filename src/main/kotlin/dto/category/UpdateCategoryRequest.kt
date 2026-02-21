package com.ktor.dto.category

import kotlinx.serialization.Serializable


@Serializable
 data class UpdateCategoryRequest (
    val name : String? = null,
    val description : String? = null
)