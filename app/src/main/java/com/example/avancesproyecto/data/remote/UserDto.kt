package com.example.avancesproyecto.data.remote

// Esta data class es el DTO (Data Transfer Object) para los usuarios.
// La uso para moldear la información de las cuentas que viaja en formato JSON
// entre la aplicación móvil de Android Studio y el backend en Spring Boot.
data class UserDto(
    // El ID se declara como opcional (puede ser nulo) porque al registrar una cuenta nueva,
    // el celular no conoce el ID; este lo autoincrementa la base de datos de Supabase.
    val id: Int?,

    // Datos obligatorios de identificación que el usuario llena en el formulario
    val name: String, // Nombre o alias del usuario
    val cif: String,  // Carnet/Código UAM. Vital para el filtro automático de roles en el backend.

    // Valores predeterminados que coinciden con las reglas de negocio de la API
    val userType: String = "ESTUDIANTE",   // Por defecto se asume rol de estudiante al crearse en la app
    val blocked: Boolean = false,         // Toda cuenta inicia activa (sin baneo) por defecto
    val registrationDate: String = ""     // Se inicializa vacío para que el backend le estampe la fecha de servidor
)