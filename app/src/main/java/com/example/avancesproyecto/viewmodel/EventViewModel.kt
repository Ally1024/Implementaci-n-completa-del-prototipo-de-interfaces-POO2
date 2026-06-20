package com.example.avancesproyecto.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.avancesproyecto.data.local.database.AppDatabase
import com.example.avancesproyecto.data.remote.RetrofitClient
import com.example.avancesproyecto.data.repository.EventRepository
import com.example.avancesproyecto.model.Event
import kotlinx.coroutines.launch
import android.util.Log

class EventViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)

    private val eventRepository = EventRepository(
        api = RetrofitClient.eventApi,
        dao = database.eventDao()
    )

    val events = mutableStateListOf<Event>()

    init {
        observeLocalEvents()
        refreshEvents()
    }

    // =======================
    // EVENTOS - OBSERVACIÓN Y SINCRONIZACIÓN
    // =======================

    private fun observeLocalEvents() {
        viewModelScope.launch {
            eventRepository.events.collect { eventList ->
                events.clear()
                events.addAll(eventList)
            }
        }
    }

    fun refreshEvents() {
        viewModelScope.launch {
            try {
                eventRepository.syncEvents()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // =======================
    // EVENTOS
    // =======================



    // AGREGAR EVENTO
    fun addEvent(
        nombre: String,
        descripcion: String,
        fecha: String,
        locacion: String,
        capacidad: Int,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        val newEvent = Event(
            id = 0,
            title = nombre,
            description = descripcion,
            date = fecha,
            location = locacion,
            maxCapacity = capacidad,
            attendees = 0,
            latitude = 0.0,
            longitude = 0.0,
            isOpen = true,
            isFeatured = false
        )

        viewModelScope.launch {
            try {
                Log.d("EVENT_DEBUG", "Intentando guardar evento: $newEvent")

                eventRepository.addEvent(newEvent)

                Log.d("EVENT_DEBUG", "Evento guardado correctamente")

                refreshEvents()

                onSuccess()

            } catch (e: Exception) {
                Log.e("EVENT_DEBUG", "Error al guardar evento", e)
                onError("No se pudo guardar el evento. Revisa la conexión con el backend.")
            }
        }
    }

    // INSCRIBIRSE
    fun joinEvent(eventId: Int) {
        val event = events.find { it.id == eventId } ?: return

        // EVENTO CERRADO
        if (!event.isOpen) return

        // EVENTO LLENO
        if (event.attendees >= event.maxCapacity) return

        viewModelScope.launch {
            try {
                eventRepository.joinEvent(eventId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    // ELIMINAR EVENTO
    fun deleteEvent(eventId: Int) {
        viewModelScope.launch {
            try {
                eventRepository.deleteEvent(eventId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    // EDITAR EVENTO
    fun editEvent(
        eventId: Int,
        title: String,
        description: String,
        date: String,
        location: String,
        capacity: Int
    ) {
        val event = events.find { it.id == eventId } ?: return

        val updatedEvent = event.copy(
            title = title,
            description = description,
            date = date,
            location = location,
            maxCapacity = capacity
        )

        viewModelScope.launch {
            try {
                eventRepository.updateEvent(updatedEvent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    // EVENTO LLENO
    fun isEventFull(event: Event): Boolean {

        return event.attendees >= event.maxCapacity
    }


    // CUPOS DISPONIBLES
    fun remainingSpots(event: Event): Int {

        return event.maxCapacity - event.attendees
    }


    // DESTACAR EVENTO
    fun toggleFeatured(eventId: Int) {
        val event = events.find { it.id == eventId } ?: return

        viewModelScope.launch {
            try {
                eventRepository.updateEvent(
                    event.copy(isFeatured = !event.isFeatured)
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    // ABRIR / CERRAR EVENTO
    fun toggleEventStatus(eventId: Int) {
        val event = events.find { it.id == eventId } ?: return

        viewModelScope.launch {
            try {
                eventRepository.updateEvent(
                    event.copy(isOpen = !event.isOpen)
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    // PORCENTAJE OCUPACION
    fun eventOccupation(event: Event): Int {

        if (event.maxCapacity == 0) return 0

        return (
                (event.attendees.toFloat() /
                        event.maxCapacity) * 100
                ).toInt()
    }

    // OBTENER EVENTO POR ID
    fun getEventById(eventId: Int): Event? {
        return events.find { it.id == eventId }
    }

}