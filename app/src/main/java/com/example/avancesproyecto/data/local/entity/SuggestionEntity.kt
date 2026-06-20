package com.example.avancesproyecto.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "suggestions")
data class SuggestionEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val maxCapacity: Int
)