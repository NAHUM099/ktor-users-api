package com.ktor.dto.common

import kotlinx.serialization.Serializable


@Serializable
data class PaginatedResponse<T : Any>(
    val data : List<T>,
    val page: Int,
    val size: Int,
    val totalElements : Long,
    val totalPages: Int
)