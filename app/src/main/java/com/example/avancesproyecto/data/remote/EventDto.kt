package com.example.avancesproyecto.data.remote

data class EventDto(
    val id: Int?,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val maxCapacity: Int,
    val attendees: Int = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val open: Boolean = true,
    val featured: Boolean = false
)