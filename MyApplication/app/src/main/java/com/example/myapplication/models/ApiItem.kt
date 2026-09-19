package com.example.myapplication.models

data class ApiItem(
    val id: String,
    val name: String,
    val description: String,
    val baseUrl: String,
    val category: String,
    val icon: String,
    val isLive: Boolean = false,
    val responseTime: Long = 0,
    val lastChecked: Long = System.currentTimeMillis(),
    val githubRepo: String = "",
    val documentation: String = "",
    val tags: List<String> = emptyList(),
    val isCustom: Boolean = false,
    val isHidden: Boolean = false
)
