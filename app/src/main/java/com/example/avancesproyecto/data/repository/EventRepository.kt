package com.example.avancesproyecto.data.repository

import com.example.avancesproyecto.data.local.dao.EventDao
import com.example.avancesproyecto.data.remote.EventApi
import com.example.avancesproyecto.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EventRepository(
    private val api: EventApi,
    private val dao: EventDao
) {

    val events: Flow<List<Event>> = dao.getEvents()
        .map { list -> list.map { it.toModel() } }

    suspend fun syncEvents() {
        val remoteEvents = api.getEvents()
        dao.insertEvents(remoteEvents.map { it.toEntity() })
    }

    suspend fun addEvent(event: Event) {
        val created = api.createEvent(event.toDto())
        dao.insertEvent(created.toEntity())
    }

    suspend fun deleteEvent(eventId: Int) {
        api.deleteEvent(eventId)
        dao.deleteEvent(eventId)
    }

    suspend fun joinEvent(eventId: Int) {
        val updated = api.joinEvent(eventId)
        dao.insertEvent(updated.toEntity())
    }

    suspend fun updateEvent(event: Event) {
        val updated = api.updateEvent(event.id, event.toDto())
        dao.insertEvent(updated.toEntity())
    }
}