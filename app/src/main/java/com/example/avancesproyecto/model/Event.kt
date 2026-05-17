package com.example.avancesproyecto.model


data class Event(

    val id: Int,

    val title: String,

    val description: String,

    val date: String,

    val location: String,

    val maxCapacity: Int,

    val attendees: Int = 0,

    val latitude: Double = 0.0,

    val longitude: Double = 0.0,

    val isOpen: Boolean = true,

    val isFeatured: Boolean = false
)