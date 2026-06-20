package com.example.avancesproyecto.data.repository

import com.example.avancesproyecto.data.local.dao.SuggestionDao
import com.example.avancesproyecto.data.remote.EventDto
import com.example.avancesproyecto.data.remote.SuggestionApi
import com.example.avancesproyecto.model.Suggestion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SuggestionRepository(
    private val api: SuggestionApi,
    private val dao: SuggestionDao
) {

    val suggestions: Flow<List<Suggestion>> = dao.getSuggestions()
        .map { list ->
            list.map { it.toModel() }
        }

    suspend fun syncSuggestions() {
        val remoteSuggestions = api.getSuggestions()

        dao.insertSuggestions(
            remoteSuggestions.map { it.toEntity() }
        )
    }

    suspend fun addSuggestion(suggestion: Suggestion) {
        val createdSuggestion = api.createSuggestion(
            suggestion.toDto()
        )

        dao.insertSuggestion(
            createdSuggestion.toEntity()
        )
    }

    suspend fun approveSuggestion(
        suggestionId: Int
    ): EventDto {
        val createdEvent = api.approveSuggestion(suggestionId)

        dao.deleteSuggestion(suggestionId)

        return createdEvent
    }

    suspend fun deleteSuggestion(suggestionId: Int) {
        api.deleteSuggestion(suggestionId)
        dao.deleteSuggestion(suggestionId)
    }
}