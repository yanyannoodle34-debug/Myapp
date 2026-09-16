package com.example.myapplication.services

import com.example.myapplication.models.GithubRateLimit
import com.example.myapplication.models.GithubSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GithubService {

    @GET("search/repositories")
    suspend fun searchRepos(
        @Query("q") query: String,
        @Query("sort") sort: String = "stars",
        @Query("order") order: String = "desc",
        @Query("per_page") perPage: Int = 20,
        @Header("Authorization") auth: String? = null,
        @Header("Accept") accept: String = "application/vnd.github+json"
    ): Response<GithubSearchResponse>

    @GET("rate_limit")
    suspend fun getRateLimit(
        @Header("Authorization") auth: String? = null
    ): Response<GithubRateLimit>
}
