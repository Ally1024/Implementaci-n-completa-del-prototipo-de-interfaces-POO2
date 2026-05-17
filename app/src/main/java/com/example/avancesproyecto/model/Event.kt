package com.example.avancesproyecto.model

data class Event(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val location: String,

    // 🔥 NUEVOS CAMPOS PARA ADMIN / CONTROL DE ASISTENCIA
    val maxCapacity: Int,        // límite total de asistentes
    val attendees: Int = 0       // inscritos actuales
)