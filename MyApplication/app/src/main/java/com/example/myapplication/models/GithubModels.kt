package com.example.myapplication.models

import com.google.gson.annotations.SerializedName

data class GithubSearchResponse(
    @SerializedName("total_count") val totalCount: Int = 0,
    @SerializedName("incomplete_results") val incompleteResults: Boolean = false,
    val items: List<GithubRepo> = emptyList()
)

data class GithubRepo(
    val id: Long = 0,
    @SerializedName("full_name") val fullName: String = "",
    val description: String? = null,
    @SerializedName("html_url") val htmlUrl: String = "",
    @SerializedName("stargazers_count") val stars: Int = 0,
    val language: String? = null,
    val topics: List<String> = emptyList(),
    val owner: GithubOwner? = null
)

data class GithubOwner(
    val login: String = "",
    @SerializedName("avatar_url") val avatarUrl: String = ""
)

data class GithubRateLimit(
    val resources: GithubResources? = null,
    val rate: GithubRate? = null
)

data class GithubResources(
    val core: GithubRate? = null,
    val search: GithubRate? = null
)

data class GithubRate(
    val limit: Int = 0,
    val remaining: Int = 0,
    val reset: Long = 0,
    val used: Int = 0
)
