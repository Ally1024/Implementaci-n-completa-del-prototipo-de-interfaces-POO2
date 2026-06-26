package com.example.avancesproyecto.data.repository

import com.example.avancesproyecto.data.local.dao.SuggestionDao
import com.example.avancesproyecto.data.remote.EventDto
import com.example.avancesproyecto.data.remote.SuggestionApi
import com.example.avancesproyecto.model.Suggestion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Este repositorio orquesta la persistencia e intercambio de las propuestas de eventos.
// Conecta el almacenamiento local de Room (SuggestionDao) con la API externa (SuggestionApi).
class SuggestionRepository(
    private val api: SuggestionApi,
    private val dao: SuggestionDao
) {

    // Expongo el flujo reactivo de sugerencias transformado a mi modelo de dominio puro (Suggestion).
    // Cada vez que ocurra un cambio en la tabla local de Room, las pantallas se refrescarán solas.
    val suggestions: Flow<List<Suggestion>> = dao.getSuggestions()
        .map { list ->
            list.map { it.toModel() }
        }

    // Sincroniza las propuestas del backend trayendo los datos más frescos y guardándolos
    // de forma masiva en Room, asegurando soporte offline para el listado.
    suspend fun syncSuggestions() {
        val remoteSuggestions = api.getSuggestions()

        dao.insertSuggestions(
            remoteSuggestions.map { it.toEntity() }
        )
    }

    // Envía una nueva sugerencia del estudiante al servidor. Al recibir la respuesta exitosa
    // con los datos validados del backend, la inserto directamente en la caché local.
    suspend fun addSuggestion(suggestion: Suggestion) {
        val createdSuggestion = api.createSuggestion(
            suggestion.toDto()
        )

        dao.insertSuggestion(
            createdSuggestion.toEntity()
        )
    }

    // Flujo clave de Administración: Aprueba una propuesta para convertirla en evento.
    // Le pego al endpoint de aprobación del servidor, el cual borra la sugerencia en la nube
    // y crea un registro en la tabla de eventos, retornándome el EventDto.
    // Por mi parte, la elimino inmediatamente de la tabla local de sugerencias y devuelvo el evento generado.
    suspend fun approveSuggestion(
        suggestionId: Int
    ): EventDto {
        val createdEvent = api.approveSuggestion(suggestionId)

        dao.deleteSuggestion(suggestionId)

        return createdEvent
    }

    // Rechaza o elimina permanentemente una propuesta de evento.
    // Impacta la API remota y limpia el registro correspondiente en la base de datos interna de Room.
    suspend fun deleteSuggestion(suggestionId: Int) {
        api.deleteSuggestion(suggestionId)
        dao.deleteSuggestion(suggestionId)
    }
}