package com.example.avancesproyecto.data.repository

import com.example.avancesproyecto.data.local.entity.SuggestionEntity
import com.example.avancesproyecto.data.remote.SuggestionDto
import com.example.avancesproyecto.model.Suggestion

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