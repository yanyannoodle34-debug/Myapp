package com.example.myapplication.models

data class ApiResponse(
    val success: Boolean,
    val statusCode: Int,
    val responseTime: Long,
    val body: String?,
    val error: String? = null
)
