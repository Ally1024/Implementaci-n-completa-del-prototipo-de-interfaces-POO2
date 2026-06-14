package com.example.avancesproyecto.data.remote

data class UserDto(
    val id: Int?,
    val name: String,
    val cif: String,
    val userType: String = "ESTUDIANTE",
    val blocked: Boolean = false,
    val registrationDate: String = ""
)