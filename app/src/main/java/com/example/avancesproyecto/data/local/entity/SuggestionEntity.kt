package com.example.avancesproyecto.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Esta clase representa la tabla "suggestions" en la base de datos interna de mi app.
// Me sirve para guardar localmente las sugerencias de eventos y poder mostrárselas al usuario al instante.
@Entity(tableName = "suggestions")
data class SuggestionEntity(
    // Defino el ID como llave primaria. Aquí ya no es opcional (no es nulo) porque
    // cuando jalo las sugerencias de la API para meterlas en la caché de Room,
    // estas ya vienen con su ID oficial asignado por el servidor.
    @PrimaryKey val id: Int,

    // Columnas que almacenan los detalles de la propuesta en el almacenamiento del celular
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val maxCapacity: Int
)