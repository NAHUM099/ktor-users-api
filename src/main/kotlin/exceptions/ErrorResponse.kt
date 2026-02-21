package com.ktor.exceptions

import kotlinx.serialization.Serializable



@Serializable
data class ErrorResponse(
    val status : Int,
    val code : String,
    val message : String,
    val timestamp: Long,
    val path : String? = null,
    val details : Map<String, String>? = null
){

companion object {
    fun from(
        status: Int,
        errorCode: ErrorCode,
        message: String,
        path: String? = null,
        details: Map<String, String>? = null
    ): ErrorResponse {
        return ErrorResponse(
            status = status,
            code = errorCode.code,
            message = message,
            timestamp = System.currentTimeMillis(),
            path = path,
            details = details
        )
    }
}
}
