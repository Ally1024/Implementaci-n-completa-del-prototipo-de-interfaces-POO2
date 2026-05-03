package com.example.avancesproyecto.viewmodel

import androidx.lifecycle.ViewModel
import com.example.avancesproyecto.model.Event
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class EventViewModel : ViewModel() {
    val events = listOf(
        Event(1, "Jornada de Limpieza del Campus", "Únete a la limpieza de áreas verdes y espacios comunes del campus para mantener un entorno saludable.", "2026-05-15", "Campus Principal"),
        Event(2, "Reforestación en el Jardín Universitario", "Participa en la plantación de árboles nativos en el jardín central de la universidad.", "2026-05-20", "Chilamate"),
        Event(3, "Taller de Reciclaje y Sostenibilidad", "Aprende prácticas de reciclaje y cómo contribuir a la sostenibilidad en el campus.", "2026-05-25", "Auditorio Central"),
        Event(4, "Campaña de Recolección de Residuos Electrónicos", "Ayuda a recolectar y reciclar residuos electrónicos para reducir el impacto ambiental.", "2026-06-01", "Plazoleta Principal"),
        Event(5, "Charla sobre Energía Renovable", "Descubre cómo la universidad promueve el uso de energías renovables.", "2026-06-05", "C-101")
    )

    private val _registeredEvents = mutableStateOf(setOf<Int>())
    val registeredEvents: State<Set<Int>> = _registeredEvents

    fun registerForEvent(eventId: Int) {
        _registeredEvents.value = _registeredEvents.value + eventId
    }

    fun unregisterFromEvent(eventId: Int) {
        _registeredEvents.value = _registeredEvents.value - eventId
    }

    fun isRegistered(eventId: Int): Boolean {
        return registeredEvents.value.contains(eventId)
    }
}
