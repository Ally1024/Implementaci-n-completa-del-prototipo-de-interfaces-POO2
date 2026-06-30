package com.example.avancesproyecto.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.avancesproyecto.data.local.database.AppDatabase
import com.example.avancesproyecto.data.remote.RetrofitClient
import com.example.avancesproyecto.data.repository.EventRepository
import com.example.avancesproyecto.data.repository.SuggestionRepository
import com.example.avancesproyecto.model.Suggestion
import kotlinx.coroutines.launch

class SuggestionViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)

    // Inicializacion de repositorios acoplados a fuentes de datos locales y remotas
    private val suggestionRepository = SuggestionRepository(
        api = RetrofitClient.suggestionApi,
        dao = database.suggestionDao()
    )

    private val eventRepository = EventRepository(
        api = RetrofitClient.eventApi,
        dao = database.eventDao()
    )

    // Estado interno mutable exclusivo para control de recomposicion en Jetpack Compose
    private val _suggestions = mutableStateListOf<Suggestion>()
    val suggestions: List<Suggestion> get() = _suggestions

    init {
        observeLocalSuggestions()
        refreshSuggestions()
    }

    // Suscripcion reactiva al flujo de datos (Flow) expuesto por el DAO local de Room
    private fun observeLocalSuggestions() {
        viewModelScope.launch {
            suggestionRepository.suggestions.collect { list ->
                _suggestions.clear()
                _suggestions.addAll(list)
            }
        }
    }

    // Fuerza la sincronizacion de la API remota hacia la base de datos local
    fun refreshSuggestions() {
        viewModelScope.launch {
            suggestionRepository.syncSuggestions()
        }
    }

    // Registra una nueva sugerencia despachandola al repositorio mediante corrutinas
    fun addSuggestion(
        title: String,
        description: String,
        date: String,
        location: String,
        capacity: Int,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val newSuggestion = Suggestion(
                    id = 0, // Identificador unico autoincremental gestionado por el backend
                    title = title,
                    description = description,
                    date = date,
                    location = location,
                    maxCapacity = capacity
                )
                suggestionRepository.addSuggestion(newSuggestion)
                onSuccess()
            } catch (e: Exception) {
                onError("Error al enviar sugerencia: ${e.message}")
            }
        }
    }

    // Transfiere una sugerencia aprobada al modulo de eventos y actualiza los repositorios implicados
    fun approveSuggestion(
        suggestionId: Int,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                suggestionRepository.approveSuggestion(suggestionId)

                // Sincronizacion transversal de las entidades afectadas
                eventRepository.syncEvents()
                suggestionRepository.syncSuggestions()

                onSuccess()
            } catch (e: Exception) {
                onError("Error al aprobar sugerencia")
            }
        }
    }

    // Remueve una propuesta rechazada del origen de datos local y remoto
    fun rejectSuggestion(
        suggestionId: Int,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                suggestionRepository.deleteSuggestion(suggestionId)
                suggestionRepository.syncSuggestions()

                onSuccess()
            } catch (e: Exception) {
                onError("Error al rechazar sugerencia")
            }
        }
    }
}