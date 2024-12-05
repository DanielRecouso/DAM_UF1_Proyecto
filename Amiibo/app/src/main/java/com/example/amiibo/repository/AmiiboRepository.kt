package com.example.amiibo.repository

import com.example.amiibo.model.Amiibo
import com.example.amiibo.network.RetrofitInstance

class AmiiboRepository {
    private val apiService = RetrofitInstance.api

    suspend fun getAmiibos(): List<Amiibo> {
        return try {
            val response = apiService.getAllAmiibo()
            response.amiibo
        } catch (e: Exception) {
            println("Error fetching amiibos: ${e.message}")
            emptyList()
        }
    }

    suspend fun getAmiibosFromCharacter(character:String?): List<Amiibo> {
        return try {
            val response = apiService.getAmiiboByCharacter(character)
            response.amiibo
        } catch (e: Exception) {
            println("Error fetching amiibos: ${e.message}")
            emptyList()
        }
    }

    suspend fun getAmiibosFromGame(game:String?): List<Amiibo> {
        return try {
            val response = apiService.getAmiiboByGame(game)
            response.amiibo
        } catch (e: Exception) {
            println("Error fetching amiibos: ${e.message}")
            emptyList()
        }
    }
}