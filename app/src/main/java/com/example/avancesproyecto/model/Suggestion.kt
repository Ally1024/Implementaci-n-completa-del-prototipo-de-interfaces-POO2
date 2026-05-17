package com.example.avancesproyecto.model

data class Suggestion(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val maxCapacity: Int,
    val message: String = ""
)