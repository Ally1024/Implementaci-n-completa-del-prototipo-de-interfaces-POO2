package com.example.avancesproyecto.model

// Clase de datos que representa las propuestas de eventos enviadas por los estudiantes
data class Suggestion(
    // Identificador unico de la sugerencia utilizado para gestionarla en el panel de control
    val id: Int,

    // Titulo propuesto para el nuevo evento ecologico
    val title: String,

    // Descripcion detallada con los objetivos o ideas de la actividad planteada
    val description: String,

    // Fecha tentativa propuesta por el alumno para realizar el evento
    val date: String,

    // Lugar sugerido dentro del campus para llevar a cabo la actividad
    val location: String,

    // Capacidad estimada de personas que podrian asistir segun el estudiante
    val maxCapacity: Int,

    // Mensaje adicional opcional, util para aclaraciones o motivos del rechazo/aprobacion
    val message: String = ""
)