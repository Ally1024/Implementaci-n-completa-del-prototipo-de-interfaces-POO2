package com.example.avancesproyecto.model

data class User(
    val id: Int,
    val name: String,
    val cif: String,
    val userType: UserType = UserType.ESTUDIANTE,
    val isBlocked: Boolean = false,
    val registrationDate: String = ""
)

enum class UserType {
    ADMIN,
    ESTUDIANTE
}


