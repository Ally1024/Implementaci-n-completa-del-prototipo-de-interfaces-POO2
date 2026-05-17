package com.example.avancesproyecto.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.avancesproyecto.model.Event

class EventViewModel : ViewModel() {

    //  Lista de eventos en memoria
    private val _events = mutableStateListOf<Event>()
    val events: List<Event> = _events


    //  CREAR EVENTO (ADMIN)
    fun addEvent(
        nombre: String,
        descripcion: String,
        fecha: String,
        locacion: String,
        capacidad: Int
    ) {
        val newEvent = Event(
            id = (_events.size + 1),
            title = nombre,
            description = descripcion,
            date = fecha,
            location = locacion,
            maxCapacity = capacidad,
            attendees = 0
        )

        _events.add(newEvent)
    }


    // INSCRIBIR USUARIO (CONTROL DE CUPO)
    fun joinEvent(eventId: Int) {

        val index = _events.indexOfFirst { it.id == eventId }

        if (index == -1) return

        val event = _events[index]

        //  si está lleno, no hace nada
        if (event.attendees >= event.maxCapacity) return

        // ✔ actualizar asistentes
        val updatedEvent = event.copy(
            attendees = event.attendees + 1
        )

        _events[index] = updatedEvent
    }


    // VERIFICAR SI ESTÁ LLENO
    fun isEventFull(event: Event): Boolean {
        return event.attendees >= event.maxCapacity
    }


    //  CUPOS DISPONIBLES
    fun remainingSpots(event: Event): Int {
        return event.maxCapacity - event.attendees
    }


    //  ELIMINAR EVENTO (ADMIN)
    fun deleteEvent(eventId: Int) {
        _events.removeAll { it.id == eventId }
    }
}