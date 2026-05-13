package com.example.avancesproyecto.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.avancesproyecto.model.Event

class EventViewModel : ViewModel() {

    private val _events = mutableStateListOf(
        Event(1, "Jornada de Limpieza del Campus",
            "Únete a la limpieza de áreas verdes y espacios comunes del campus para mantener un entorno saludable.",
            "2026-05-15", "08:00", "Universidad Americana UAM, Managua, Nicaragua"),
        Event(2, "Reforestación en el Jardín Universitario",
            "Participa en la plantación de árboles nativos en el jardín central de la universidad.",
            "2026-05-20", "09:00", "Chilamate, Sarapiquí, Costa Rica"),
        Event(3, "Taller de Reciclaje y Sostenibilidad",
            "Aprende prácticas de reciclaje y cómo contribuir a la sostenibilidad en el campus.",
            "2026-05-25", "10:00", "Auditorio Central UAM, Managua, Nicaragua"),
        Event(4, "Campaña de Residuos Electrónicos",
            "Ayuda a recolectar y reciclar residuos electrónicos para reducir el impacto ambiental.",
            "2026-06-01", "08:30", "Plazoleta Principal UAM, Managua, Nicaragua"),
        Event(5, "Charla sobre Energía Renovable",
            "Descubre cómo la universidad promueve el uso de energías renovables.",
            "2026-06-05", "14:00", "Salón C-101 UAM, Managua, Nicaragua")
    )
    val events: List<Event> get() = _events
    private var nextId = 6

    // eventId -> Set<userId>
    private val _registrations = mutableStateOf<Map<Int, Set<Int>>>(emptyMap())
    val registrations: State<Map<Int, Set<Int>>> = _registrations

    // eventId -> userId -> asistió (true/false)
    private val _attendance = mutableStateOf<Map<Int, Map<Int, Boolean>>>(emptyMap())
    val attendance: State<Map<Int, Map<Int, Boolean>>> = _attendance

    // ── EVENTOS ────────────────────────────────────────
    fun addEvent(title: String, description: String, date: String,
                 time: String, location: String, imageUri: String?) {
        _events.add(Event(nextId++, title, description, date, time, location, imageUri))
    }

    fun deleteEvent(id: Int) {
        _events.removeAll { it.id == id }
        _registrations.value = _registrations.value - id
        _attendance.value    = _attendance.value - id
    }

    fun getEventById(id: Int): Event? = _events.find { it.id == id }

    // ── INSCRIPCIONES ──────────────────────────────────
    fun registerForEvent(eventId: Int, userId: Int) {
        val map = _registrations.value.toMutableMap()
        map[eventId] = (map[eventId] ?: emptySet()) + userId
        _registrations.value = map
    }

    fun unregisterFromEvent(eventId: Int, userId: Int) {
        val map = _registrations.value.toMutableMap()
        map[eventId] = (map[eventId] ?: emptySet()) - userId
        _registrations.value = map
    }

    fun isRegistered(eventId: Int, userId: Int): Boolean =
        _registrations.value[eventId]?.contains(userId) == true

    fun getRegisteredUserIds(eventId: Int): Set<Int> =
        _registrations.value[eventId] ?: emptySet()

    // ── ASISTENCIA ─────────────────────────────────────
    fun markAttendance(eventId: Int, userId: Int, attended: Boolean) {
        val outer = _attendance.value.toMutableMap()
        val inner = (outer[eventId] ?: emptyMap()).toMutableMap()
        inner[userId]    = attended
        outer[eventId]   = inner
        _attendance.value = outer
    }

    fun getAttendance(eventId: Int, userId: Int): Boolean? =
        _attendance.value[eventId]?.get(userId)

    fun getUserAttendedEvents(userId: Int): List<Event> =
        _events.filter { _attendance.value[it.id]?.get(userId) == true }

    fun getTotalAttendedCount(userId: Int): Int =
        _attendance.value.values.count { it[userId] == true }

    // Cuántos eventos ha asistido cada alumno (para el dashboard admin)
    fun getAttendanceSummary(): Map<Int, Int> {
        val summary = mutableMapOf<Int, Int>()
        _attendance.value.forEach { (_, userMap) ->
            userMap.forEach { (userId, attended) ->
                if (attended) summary[userId] = (summary[userId] ?: 0) + 1
            }
        }
        return summary
    }
}
