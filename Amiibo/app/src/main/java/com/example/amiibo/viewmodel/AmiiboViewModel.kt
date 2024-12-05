package com.example.amiibo.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.amiibo.model.Amiibo
import com.example.amiibo.repository.AmiiboRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

class AmiiboViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("amiibo_prefs", Context.MODE_PRIVATE)
    private val amiiboRepository = AmiiboRepository()
    private val gson = Gson()

    private val _amiibos = MutableLiveData<List<Amiibo>>()
    val amiibos: LiveData<List<Amiibo>> get() = _amiibos

    private val _selectedAmiibos = MutableLiveData<List<Amiibo>>()
    val selectedAmiibos: LiveData<List<Amiibo>> get() = _selectedAmiibos


    init {
        fetchAmiibos()
        val savedFavorites = getSelectedAmiibos()
        if (savedFavorites.isNotEmpty()) {
            _amiibos.value = savedFavorites
        }

    }

    private fun fetchAmiibos() {
        viewModelScope.launch {
            try {
                val amiiboList = amiiboRepository.getAmiibos()
                _amiibos.postValue(amiiboList)
            } catch (e: Exception) {
                // Handle errors
            }
        }
    }

    fun fetchAmiibosFromCharacter(character: String?) {
        viewModelScope.launch {
            try {
                val amiiboList = amiiboRepository.getAmiibosFromCharacter(character)
                _amiibos.postValue(amiiboList)
            } catch (e: Exception) {
                // Handle errors
            }
        }
    }

    fun fetchAmiibosFromGame(game: String?) {
        viewModelScope.launch {
            try {
                val amiiboList = amiiboRepository.getAmiibosFromGame(game)
                _amiibos.postValue(amiiboList)
            } catch (e: Exception) {
                // Handle errors
            }
        }
    }

    fun saveSelectedAmiibos(selectedAmiibos: List<Amiibo>) {
        val json = gson.toJson(selectedAmiibos)
        sharedPreferences.edit().putString("selected_amiibos", json).apply()
    }

    fun getSelectedAmiibos(): List<Amiibo> {
        val json = sharedPreferences.getString("selected_amiibos", null)
        return if (json != null) {
            try {
                val type = object : TypeToken<List<Amiibo>>() {}.type
                gson.fromJson(json, type)
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        } else {
            emptyList()
        }
    }


}
