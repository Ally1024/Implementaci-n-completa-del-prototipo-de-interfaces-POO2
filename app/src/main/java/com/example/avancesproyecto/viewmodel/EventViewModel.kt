package com.example.avancesproyecto.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avancesproyecto.model.Event
import kotlinx.coroutines.launch

class EventViewModel : ViewModel() {

    // Coleccion observable por Jetpack Compose con setter privado para proteger la encapsulacion
    var events by mutableStateOf<List<Event>>(emptyList())
        private set

    init {
        refreshEvents()
    }

    // Sincroniza la coleccion de datos consumiendo el endpoint correspondiente en el backend
    fun refreshEvents() {
        viewModelScope.launch {
            try {
                // Implementacion de llamada remota estructurada:
                // val response = RetrofitClient.api.getAllEvents()
                // events = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Incrementa el aforo del evento tanto en el servidor remoto como en el estado local reactivo
    fun joinEvent(eventId: Int) {
        viewModelScope.launch {
            try {
                // Envio de la peticion POST a la API (/api/events/{id}/join):
                // RetrofitClient.api.joinEvent(eventId)

                // Actualizacion inmediata del estado local para optimizar la respuesta visual en Compose
                events = events.map { event ->
                    if (event.id == eventId) {
                        event.copy(attendees = event.attendees + 1)
                    } else {
                        event
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Persiste un nuevo evento en el repositorio de datos y refresca el listado global
    fun addEvent(title: String, description: String, location: String, date: String, maxCapacity: Int) {
        viewModelScope.launch {
            try {
                val newEvent = Event(
                    id = 0, // Identificador autoincremental gestionado por el backend
                    title = title,
                    description = description,
                    location = location,
                    date = date,
                    maxCapacity = maxCapacity,
                    attendees = 0,
                    isOpen = true,
                    isFeatured = false
                )
                // Persistencia mediante cliente HTTP:
                // RetrofitClient.api.createEvent(newEvent)

                events = events + newEvent
                refreshEvents()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Actualiza un evento existente tanto en el servidor como en el estado local
    fun editEvent(eventId: Int, title: String, description: String, date: String, location: String, maxCapacity: Int) {
        viewModelScope.launch {
            try {
                // Envio de la peticion PUT/PATCH a la API:
                // RetrofitClient.api.updateEvent(eventId, updatedEvent)

                // Actualizacion del estado local reactivo
                events = events.map { event ->
                    if (event.id == eventId) {
                        event.copy(
                            title = title,
                            description = description,
                            date = date,
                            location = location,
                            maxCapacity = maxCapacity
                        )
                    } else {
                        event
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Modifica el estado de desatado del evento aplicando mutacion segura sobre la lista inmutable
    fun toggleFeatured(eventId: Int) {
        events = events.map { event ->
            if (event.id == eventId) {
                val updated = event.copy(isFeatured = !event.isFeatured)
                // viewModelScope.launch { RetrofitClient.api.updateEvent(eventId, updated) }
                updated
            } else {
                event
            }
        }
    }

    // Altera el estado de admision o cierre de inscripciones del evento seleccionado
    fun toggleEventStatus(eventId: Int) {
        events = events.map { event ->
            if (event.id == eventId) {
                val updated = event.copy(isOpen = !event.isOpen)
                // viewModelScope.launch { RetrofitClient.api.updateEvent(eventId, updated) }
                updated
            } else {
                event
            }
        }
    }

    // Elimina un evento de la coleccion local y potencialmente del servidor remoto
    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            try {
                // Sincronizacion con el backend (Simulado):
                // RetrofitClient.api.deleteEvent(event.id)

                // Actualizacion del estado reactivo eliminando el objeto de la lista
                events = events.filter { it.id != event.id }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Calcula de manera matematica el porcentaje de ocupacion del aforo actual
    fun eventOccupation(event: Event): Int {
        if (event.maxCapacity == 0) return 0
        return (event.attendees * 100) / event.maxCapacity
    }
}