package com.example.avancesproyecto.data.remote

// Esta data class funciona como mi DTO (Data Transfer Object).
// La uso para moldear y recibir los datos en JSON que me manda la API de Spring Boot
// y transformarlos fácilmente en objetos nativos de Kotlin.
data class EventDto(
    // El ID es opcional (puede ser nulo) porque cuando creo un evento nuevo,
    // el celular no sabe el ID; es la base de datos en Supabase la que lo genera.
    val id: Int?,

    // Datos principales que me exige la vista del evento
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val maxCapacity: Int,

    // Valores por defecto idénticos a la lógica de mi backend para evitar campos vacíos (nulls)
    val attendees: Int = 0,         // Inicia sin personas registradas
    val latitude: Double = 0.0,     // Coordenadas por defecto para el mapa
    val longitude: Double = 0.0,
    val open: Boolean = true,       // El evento se crea abierto por defecto
    val featured: Boolean = false   // No es destacado a menos que se indique lo contrario
)