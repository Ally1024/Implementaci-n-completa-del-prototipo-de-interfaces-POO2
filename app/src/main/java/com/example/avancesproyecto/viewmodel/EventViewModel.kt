package com.example.avancesproyecto.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.avancesproyecto.model.Event

class EventViewModel : ViewModel() {

    private val _events = mutableStateOf(

        listOf(

            Event(
                1,
                "Jornada de Limpieza del Campus",
                "Únete a la limpieza de áreas verdes y espacios comunes del campus para mantener un entorno saludable.",
                "2026-05-15",
                "Campus Principal"
            ),

            Event(
                2,
                "Reforestación en el Jardín Universitario",
                "Participa en la plantación de árboles nativos en el jardín central de la universidad.",
                "2026-05-20",
                "Chilamate"
            ),

            Event(
                3,
                "Taller de Reciclaje y Sostenibilidad",
                "Aprende prácticas de reciclaje y cómo contribuir a la sostenibilidad en el campus.",
                "2026-05-25",
                "Auditorio Central"
            )
        )
    )

    val events: State<List<Event>> = _events

    private val _registeredEvents =
        mutableStateOf(setOf<Int>())

    val registeredEvents:
            State<Set<Int>> = _registeredEvents

    fun registerForEvent(eventId: Int) {

        _registeredEvents.value =
            _registeredEvents.value + eventId
    }

    fun unregisterFromEvent(eventId: Int) {

        _registeredEvents.value =
            _registeredEvents.value - eventId
    }

    fun addEvent(
        title: String,
        description: String,
        date: String,
        location: String
    ) {

        val newEvent = Event(

            id = _events.value.size + 1,

            title = title,

            description = description,

            date = date,

            location = location
        )

        _events.value =
            _events.value + newEvent
    }
}