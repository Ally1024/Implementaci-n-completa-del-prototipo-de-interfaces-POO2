package com.example.avancesproyecto.data.repository

import com.example.avancesproyecto.data.local.entity.EventEntity
import com.example.avancesproyecto.data.remote.EventDto
import com.example.avancesproyecto.model.Event

// =============================================================================
// FUNCIONES DE EXTENSIÓN (MAPPERS)
// Creé estos traductores para desacoplar mi código. Así, cada capa de la app
// trabaja solo con el tipo de dato que le corresponde, manteniendo la arquitectura limpia.
// =============================================================================

// Convierte un objeto que viene de la red (API) a un formato apto para la base de datos local (Room).
// Si el ID viene nulo desde el servidor, le asigno un 0 por defecto para que Room lo procese bien.
fun EventDto.toEntity(): EventEntity {
    return EventEntity(
        id = id ?: 0,
        title = title,
        description = description,
        date = date,
        location = location,
        maxCapacity = maxCapacity,
        attendees = attendees,
        latitude = latitude,
        longitude = longitude,
        isOpen = open,       // Mapeo 'open' (API) a 'isOpen' (Room)
        isFeatured = featured // Mapeo 'featured' (API) a 'isFeatured' (Room)
    )
}

// Convierte un registro de la base de datos local (Room) al modelo de dominio puro (Event).
// Este modelo limpio es el que va a consumir mi ViewModel y el que se va a pintar en las pantallas (Compose/Views).
fun EventEntity.toModel(): Event {
    return Event(
        id = id,
        title = title,
        description = description,
        date = date,
        location = location,
        maxCapacity = maxCapacity,
        attendees = attendees,
        latitude = latitude,
        longitude = longitude,
        isOpen = isOpen,
        isFeatured = isFeatured
    )
}

// Convierte mi modelo de interfaz/dominio (Event) de vuelta a un DTO de red (EventDto).
// Lo utilizo cuando el usuario realiza una acción en el celular (como crear o modificar un evento)
// y necesito empaquetar los datos en el formato JSON exacto que espera mi backend de Spring Boot.
fun Event.toDto(): EventDto {
    return EventDto(
        id = id,
        title = title,
        description = description,
        date = date,
        location = location,
        maxCapacity = maxCapacity,
        attendees = attendees,
        latitude = latitude,
        longitude = longitude,
        open = isOpen,       // Traduzco de vuelta al nombre que requiere el JSON de la API
        featured = isFeatured
    )
}