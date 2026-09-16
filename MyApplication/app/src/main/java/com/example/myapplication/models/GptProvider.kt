package com.example.myapplication.models

data class GptProvider(
    val id: String,
    val name: String,
    val baseUrl: String,
    val model: String,
    val apiKeyRequired: Boolean,
    val isFree: Boolean,
    val description: String
)
