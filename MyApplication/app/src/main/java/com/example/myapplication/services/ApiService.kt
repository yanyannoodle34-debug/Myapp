package com.example.myapplication.services

import com.example.myapplication.models.GptRequest
import com.example.myapplication.models.GptResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET
    suspend fun testApi(@Url url: String): Response<Any>

    @GET
    suspend fun testApiGetString(@Url url: String): Response<String>

    @POST
    suspend fun sendGptRequest(
        @Url url: String,
        @Header("Authorization") auth: String,
        @Body request: GptRequest
    ): Response<GptResponse>
}
