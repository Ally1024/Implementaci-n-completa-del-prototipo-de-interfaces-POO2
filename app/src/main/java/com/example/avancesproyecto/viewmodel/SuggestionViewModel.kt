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

    private val suggestionRepository = SuggestionRepository(
        api = RetrofitClient.suggestionApi,
        dao = database.suggestionDao()
    )

    /*
       Se usa para actualizar Room de eventos cuando
       un administrador aprueba una sugerencia.
    */
    private val eventRepository = EventRepository(
        api = RetrofitClient.eventApi,
        dao = database.eventDao()
    )

    private val _suggestions = mutableStateListOf<Suggestion>()

    val suggestions: List<Suggestion>
        get() = _suggestions

    init {
        observeLocalSuggestions()
        refreshSuggestions()
    }

    private fun observeLocalSuggestions() {
        viewModelScope.launch {
            suggestionRepository.suggestions.collect { suggestionList ->
                _suggestions.clear()
                _suggestions.addAll(suggestionList)
            }
        }
    }

    fun refreshSuggestions() {
        viewModelScope.launch {
            try {
                suggestionRepository.syncSuggestions()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addSuggestion(
        title: String,
        description: String,
        date: String,
        location: String,
        capacity: Int,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        val suggestion = Suggestion(
            id = 0,
            title = title,
            description = description,
            date = date,
            location = location,
            maxCapacity = capacity
        )

        viewModelScope.launch {
            try {
                suggestionRepository.addSuggestion(suggestion)
                refreshSuggestions()
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
                onError("No se pudo enviar la sugerencia.")
            }
        }
    }

    fun approveSuggestion(
        suggestionId: Int,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                suggestionRepository.approveSuggestion(suggestionId)

                /*
                   El backend ya creó el Event.
                   Sincronizamos Room para que EventViewModel
                   detecte el nuevo evento automáticamente.
                */
                eventRepository.syncEvents()

                refreshSuggestions()
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
                onError("No se pudo aprobar la sugerencia.")
            }
        }
    }

    fun rejectSuggestion(
        suggestionId: Int,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                suggestionRepository.deleteSuggestion(suggestionId)
                refreshSuggestions()
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
                onError("No se pudo rechazar la sugerencia.")
            }
        }
    }
}