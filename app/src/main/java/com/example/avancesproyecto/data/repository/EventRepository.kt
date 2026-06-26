package com.example.avancesproyecto.data.repository

import com.example.avancesproyecto.data.local.dao.EventDao
import com.example.avancesproyecto.data.remote.EventApi
import com.example.avancesproyecto.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Este repositorio une mi fuente de datos remota (Retrofit) con mi base de datos local (Room).
// Centralizo aquí toda la lógica de sincronización e inyecto la API y el DAO como dependencias.
class EventRepository(
    private val api: EventApi,
    private val dao: EventDao
) {

    // Expongo el flujo (Flow) de eventos de manera reactiva hacia el ViewModel/UI.
    // Tomo las entidades crudas de Room, y mediante '.map' las transformo en mis modelos
    // puros de dominio (Event) usando la función de extensión 'toModel()' que creé.
    val events: Flow<List<Event>> = dao.getEvents()
        .map { list -> list.map { it.toModel() } }

    // Sincroniza los datos: viaja a la red por los eventos frescos de Spring Boot y,
    // usando 'toEntity()', los guarda de golpe en la base de datos local para la persistencia offline.
    suspend fun syncEvents() {
        val remoteEvents = api.getEvents()
        dao.insertEvents(remoteEvents.map { it.toEntity() })
    }

    // Registra un evento nuevo: se lo manda primero al servidor en formato DTO.
    // Al recibir la respuesta con el ID oficial, lo guardo en la base de datos local de inmediato.
    suspend fun addEvent(event: Event) {
        val created = api.createEvent(event.toDto())
        dao.insertEvent(created.toEntity())
    }

    // Borra un evento por completo: primero le pega al endpoint de borrado de Spring Boot
    // y acto seguido lo destruye de la base de datos interna para que desaparezca de la pantalla.
    suspend fun deleteEvent(eventId: Int) {
        api.deleteEvent(eventId)
        dao.deleteEvent(eventId)
    }

    // Acción para unirse a un evento: manda la petición POST especial al endpoint '/join',
    // recibe el evento modificado (con el contador de asistentes actualizado) y sobreescribe la caché local.
    suspend fun joinEvent(eventId: Int) {
        val updated = api.joinEvent(eventId)
        dao.insertEvent(updated.toEntity())
    }

    // Edita un evento existente: le envía al backend el ID por URL y el DTO modificado en el cuerpo.
    // La respuesta actualizada del servidor la guarda en Room para refrescar la interfaz.
    suspend fun updateEvent(event: Event) {
        val updated = api.updateEvent(event.id, event.toDto())
        dao.insertEvent(updated.toEntity())
    }
}