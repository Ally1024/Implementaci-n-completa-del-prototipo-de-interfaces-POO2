package com.example.avancesproyecto.model

data class Event(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,           // YYYY-MM-DD
    val time: String = "08:00", // HH:MM
    val location: String,
    val imageUri: String? = null,
    val maxCapacity: Int = 50
)
