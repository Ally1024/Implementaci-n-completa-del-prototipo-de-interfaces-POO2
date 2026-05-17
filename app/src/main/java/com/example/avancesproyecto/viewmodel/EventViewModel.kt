package com.example.avancesproyecto.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.avancesproyecto.model.Event
import com.example.avancesproyecto.model.Suggestion
import com.example.avancesproyecto.model.User
import com.example.avancesproyecto.model.UserType

class EventViewModel : ViewModel() {

    // =======================
    // EVENTOS
    // =======================

    private val _events = mutableStateListOf<Event>()

    val events: List<Event>
        get() = _events


    // AGREGAR EVENTO
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


    // INSCRIBIRSE
    fun joinEvent(eventId: Int) {

        val index = _events.indexOfFirst {
            it.id == eventId
        }

        if (index == -1) return

        val event = _events[index]

        // EVENTO CERRADO
        if (!event.isOpen) return

        // EVENTO LLENO
        if (event.attendees >= event.maxCapacity) return

        _events[index] = event.copy(
            attendees = event.attendees + 1
        )
    }


    // ELIMINAR EVENTO
    fun deleteEvent(eventId: Int) {

        _events.removeAll {
            it.id == eventId
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

        val index = _events.indexOfFirst {
            it.id == eventId
        }

        if (index == -1) return

        val oldEvent = _events[index]

        _events[index] = oldEvent.copy(

            title = title,

            description = description,

            date = date,

            location = location,

            maxCapacity = capacity
        )
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

        val index = _events.indexOfFirst {
            it.id == eventId
        }

        if (index == -1) return

        val event = _events[index]

        _events[index] = event.copy(
            isFeatured = !event.isFeatured
        )
    }


    // ABRIR / CERRAR EVENTO
    fun toggleEventStatus(eventId: Int) {

        val index = _events.indexOfFirst {
            it.id == eventId
        }

        if (index == -1) return

        val event = _events[index]

        _events[index] = event.copy(
            isOpen = !event.isOpen
        )
    }


    // PORCENTAJE OCUPACION
    fun eventOccupation(event: Event): Int {

        if (event.maxCapacity == 0) return 0

        return (
                (event.attendees.toFloat() /
                        event.maxCapacity) * 100
                ).toInt()
    }


    // =======================
    // SUGERENCIAS
    // =======================

    private val _suggestions =
        mutableStateListOf<Suggestion>()

    val suggestions: List<Suggestion>
        get() = _suggestions


    // AGREGAR SUGERENCIA
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


    // APROBAR
    fun approveSuggestion(
        suggestion: Suggestion
    ) {

        addEvent(

            nombre = suggestion.title,

            descripcion = suggestion.description,

            fecha = suggestion.date,

            locacion = suggestion.location,

            capacidad = suggestion.maxCapacity
        )

        _suggestions.remove(suggestion)
    }


    // RECHAZAR
    fun rejectSuggestion(
        suggestion: Suggestion
    ) {

        _suggestions.remove(suggestion)
    }


    // LIMPIAR SUGERENCIAS
    fun clearSuggestions() {

        _suggestions.clear()
    }


    // =======================
    // USUARIOS
    // =======================

    private val _users = mutableStateListOf<User>()

    val users: List<User>
        get() = _users

    // INICIALIZAR USUARIOS DE PRUEBA
    fun initializeUsers() {
        // Los usuarios solo se agregan mediante registro
    }

    // AGREGAR USUARIO
    fun addUser(
        name: String,
        cif: String,
        userType: UserType = UserType.ESTUDIANTE
    ) {
        val newUser = User(
            id = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
            name = name,
            cif = cif,
            userType = userType,
            registrationDate = getCurrentDate()
        )
        _users.add(newUser)
    }

    // ELIMINAR USUARIO
    fun deleteUser(userId: Int) {
        _users.removeAll { it.id == userId }
    }

    // BLOQUEAR/DESBLOQUEAR USUARIO
    fun toggleBlockUser(userId: Int) {
        val index = _users.indexOfFirst { it.id == userId }
        if (index == -1) return

        val user = _users[index]
        _users[index] = user.copy(isBlocked = !user.isBlocked)
    }

    // OBTENER USUARIO POR ID
    fun getUser(userId: Int): User? {
        return _users.find { it.id == userId }
    }

    // CONTAR USUARIOS ACTIVOS
    fun countActiveUsers(): Int {
        return _users.count { !it.isBlocked }
    }

    // CONTAR USUARIOS BLOQUEADOS
    fun countBlockedUsers(): Int {
        return _users.count { it.isBlocked }
    }

    // CONTAR USUARIOS POR TIPO
    fun countUsersByType(userType: UserType): Int {
        return _users.count { it.userType == userType }
    }

    // FILTRAR USUARIOS POR BÚSQUEDA
    fun searchUsers(query: String): List<User> {
        if (query.isEmpty()) return _users
        return _users.filter {
            it.name.contains(query, ignoreCase = true) ||
            it.cif.contains(query, ignoreCase = true)
        }
    }

    // FUNCIÓN AUXILIAR PARA OBTENER FECHA ACTUAL
    private fun getCurrentDate(): String {
        val calendar = java.util.Calendar.getInstance()
        val year = calendar.get(java.util.Calendar.YEAR)
        val month = calendar.get(java.util.Calendar.MONTH) + 1
        val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)
        return String.format("%04d-%02d-%02d", year, month, day)
    }
}