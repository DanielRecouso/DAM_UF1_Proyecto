package com.example.amiibo.network

import com.example.amiibo.model.AmiiboResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AmiiboApi {
    // Fetch all Amiibo
    @GET("amiibo/")
    suspend fun getAllAmiibo(): AmiiboResponse

    @GET("amiibo/")
    suspend fun getAmiiboByCharacter(@Query("name") character: String?): AmiiboResponse

    @GET("amiibo/")
    suspend fun getAmiiboByGame(@Query("amiiboSeries") character: String?): AmiiboResponse
}