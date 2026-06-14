package com.example.avancesproyecto.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.avancesproyecto.model.UserType

@Entity (tableName = "TblUsers")
data class UserEntity (
    @PrimaryKey val id: Int,
    val name: String,
    val cif: String,
    val userType: UserType = UserType.ESTUDIANTE,
    val isBlocked: Boolean = false,
    val registrationDate: String = ""
)