package com.example.avancesproyecto.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Defino que esta clase va a ser una tabla real dentro de la base de datos local de SQLite de Room.
// Le asigno explícitamente el nombre de tabla "events" para mantener orden con respecto a mi backend.
@Entity(tableName = "events")
data class EventEntity(
    // Defino la llave primaria. Aquí el ID no es opcional (no es nulo) porque para estar guardado
    // en la caché local, obligatoriamente debió venir ya registrado y estructurado desde el backend.
    @PrimaryKey val id: Int,

    // Columnas que van a almacenar los datos descriptivos del evento en el celular
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val maxCapacity: Int,

    // Valores por defecto para asegurar que Room guarde datos válidos si la API devuelve campos vacíos
    val attendees: Int = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val isOpen: Boolean = true,      // Estado de disponibilidad del evento local
    val isFeatured: Boolean = false   // Indica si el evento debe renderizarse en el banner destacado
)