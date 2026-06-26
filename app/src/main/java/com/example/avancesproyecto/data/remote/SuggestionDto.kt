package com.example.avancesproyecto.data.remote

// Esta data class representa el DTO (Data Transfer Object) para las sugerencias.
// La uso para estructurar los datos de las propuestas de eventos que envían los usuarios
// y mapear los JSON que intercambia la app con el backend de Spring Boot.
data class SuggestionDto(
    // El ID es opcional (puede ser nulo) porque cuando un estudiante crea una propuesta desde la app,
    // el backend se encarga de asignarle su ID autoincremental en Supabase.
    val id: Int?,

    // Campos requeridos para proponer un evento a revisión de los administradores
    val title: String,          // Nombre sugerido para el evento
    val description: String,    // Explicación o detalles de la propuesta (soporta hasta 1000 caracteres en la BD)
    val date: String,           // Fecha propuesta para realizarlo
    val location: String,       // Lugar físico sugerido dentro o fuera del campus
    val maxCapacity: Int        // Cantidad máxima estimada de asistentes
)