package com.example.avancesproyecto.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "events")
data class EventEntity (
    @PrimaryKey val id: Int,
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
