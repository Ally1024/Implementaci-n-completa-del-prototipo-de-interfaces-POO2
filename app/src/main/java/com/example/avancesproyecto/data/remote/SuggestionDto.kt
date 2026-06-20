package com.example.avancesproyecto.data.remote

data class SuggestionDto(
    val id: Int?,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val maxCapacity: Int
)