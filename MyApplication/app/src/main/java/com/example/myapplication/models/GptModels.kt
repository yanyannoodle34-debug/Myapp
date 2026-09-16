package com.example.myapplication.models

data class GptRequest(
    val model: String,
    val messages: List<GptMessage>,
    val temperature: Double = 0.7,
    val maxTokens: Int = 1000
)

data class GptMessage(
    val role: String,
    val content: String
)

data class GptResponse(
    val choices: List<GptChoice>?
)

data class GptChoice(
    val message: GptMessage?
)
