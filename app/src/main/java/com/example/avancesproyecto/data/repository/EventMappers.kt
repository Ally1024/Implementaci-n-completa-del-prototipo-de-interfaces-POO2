package com.example.avancesproyecto.data.repository

import com.example.avancesproyecto.data.local.entity.EventEntity
import com.example.avancesproyecto.data.remote.EventDto
import com.example.avancesproyecto.model.Event

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
        isOpen = open,
        isFeatured = featured
    )
}

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
        open = isOpen,
        featured = isFeatured
    )
}