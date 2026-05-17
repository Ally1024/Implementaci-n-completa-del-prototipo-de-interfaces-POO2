package com.example.avancesproyecto.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.avancesproyecto.model.Event
import com.example.avancesproyecto.model.Suggestion

class EventViewModel : ViewModel() {

    // =======================
    //  EVENTOS
    // =======================

    private val _events = mutableStateListOf<Event>()
    val events: List<Event> get() = _events


    fun addEvent(
        nombre: String,
        descripcion: String,
        fecha: String,
        locacion: String,
        capacidad: Int
    ) {
        val newEvent = Event(
            id = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
            title = nombre,
            description = descripcion,
            date = fecha,
            location = locacion,
            maxCapacity = capacidad,
            attendees = 0
        )

        _events.add(newEvent)
    }


    fun joinEvent(eventId: Int) {
        val index = _events.indexOfFirst { it.id == eventId }
        if (index == -1) return

        val event = _events[index]

        if (event.attendees >= event.maxCapacity) return

        _events[index] = event.copy(
            attendees = event.attendees + 1
        )
    }


    fun deleteEvent(eventId: Int) {
        _events.removeAll { it.id == eventId }
    }


    fun isEventFull(event: Event) =
        event.attendees >= event.maxCapacity


    fun remainingSpots(event: Event) =
        event.maxCapacity - event.attendees


    // =======================
    // 💡 SUGERENCIAS
    // =======================

    private val _suggestions = mutableStateListOf<Suggestion>()
    val suggestions: List<Suggestion> get() = _suggestions


    fun addSuggestion(
        title: String,
        description: String,
        date: String,
        location: String,
        capacity: Int
    ) {
        val suggestion = Suggestion(
            id = (_suggestions.size + 1),
            title = title,
            description = description,
            date = date,
            location = location,
            maxCapacity = capacity
        )

        _suggestions.add(suggestion)
    }


    fun approveSuggestion(suggestion: Suggestion) {

        addEvent(
            nombre = suggestion.title,
            descripcion = suggestion.description,
            fecha = suggestion.date,
            locacion = suggestion.location,
            capacidad = suggestion.maxCapacity
        )

        _suggestions.remove(suggestion)
    }


    fun rejectSuggestion(suggestion: Suggestion) {
        _suggestions.remove(suggestion)
    }
}