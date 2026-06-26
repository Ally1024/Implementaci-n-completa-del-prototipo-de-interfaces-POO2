package com.example.avancesproyecto.data.repository

import com.example.avancesproyecto.data.local.entity.SuggestionEntity
import com.example.avancesproyecto.data.remote.SuggestionDto
import com.example.avancesproyecto.model.Suggestion

// =============================================================================
// MAPPERS PARA SUGERENCIAS (Traducción de Datos)
// Con estas funciones de extensión separo limpiamente el formato que maneja la red (DTO),
// el formato persistido localmente (Entity) y el objeto de negocio para la UI (Model).
// =============================================================================

// Convierte el DTO que viene del servidor (Spring Boot) en una entidad para guardar en Room.
// Si por alguna razón el ID de la red viniera nulo, le asigno 0 por defecto para cumplir con la llave primaria de SQLite.
fun SuggestionDto.toEntity(): SuggestionEntity {
    return SuggestionEntity(
        id = id ?: 0,
        title = title,
        description = description,
        date = date,
        location = location,
        maxCapacity = maxCapacity
    )
}

// Convierte la entidad almacenada en Room a mi modelo de dominio puro 'Suggestion'.
// Este es el modelo desacoplado que mis ViewModels van a exponer directamente a las pantallas de Jetpack Compose.
fun SuggestionEntity.toModel(): Suggestion {
    return Suggestion(
        id = id,
        title = title,
        description = description,
        date = date,
        location = location,
        maxCapacity = maxCapacity
    )
}

// Convierte el modelo de la interfaz de usuario (Suggestion) a un DTO de red listo para ser enviado en un JSON.
// OJO a este detalle clave: si el ID es 0 (lo que significa que es una sugerencia nueva creada en el cel que aún no tiene ID real),
// le pasamos un 'null' para que la base de datos remota (Supabase) entienda que debe autoincrementarlo.
fun Suggestion.toDto(): SuggestionDto {
    return SuggestionDto(
        id = if (id == 0) null else id,
        title = title,
        description = description,
        date = date,
        location = location,
        maxCapacity = maxCapacity
    )
}