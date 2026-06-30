package com.example.avancesproyecto.model

// Clase de datos central que representa la estructura logica de un evento ecológico en el sistema
data class Event(

    // Identificador unico del evento utilizado para la navegacion parametrizada y llaves de listas
    val id: Int,

    // Nombre principal del evento que se muestra en los encabezados de las tarjetas
    val title: String,

    // Detalle extenso sobre las actividades o requisitos del evento ambiental
    val description: String,

    // Fecha programada para la realizacion de la actividad
    val date: String,

    // Ubicacion fisica o espacio asignado dentro del campus de la UAM
    val location: String,

    // Limite maximo permitido de estudiantes inscritos para controlar el aforo
    val maxCapacity: Int,

    // Contador de personas registradas actualmente que incrementa al confirmar la inscripcion
    val attendees: Int = 0,

    // Coordenada de latitud para la integracion posterior con mapas o geolocalizacion
    val latitude: Double = 0.0,

    // Coordenada de longitud para la integracion posterior con mapas o geolocalizacion
    val longitude: Double = 0.0,

    // Bandera que define si el evento admite mas inscripciones o si fue cerrado por el administrador
    val isOpen: Boolean = true,

    // Atributo para fijar el evento en la seccion superior como destacado dentro del panel y home
    val isFeatured: Boolean = false
)