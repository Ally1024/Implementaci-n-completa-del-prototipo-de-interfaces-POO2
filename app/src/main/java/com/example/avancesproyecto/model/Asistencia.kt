package com.example.avancesproyecto.model

// Clase de datos ligera para mapear los asistentes que se van a mostrar directamente en la interfaz grafica
data class Asistente(
    // Identificador unico del asistente para diferenciarlo en las listas mutables de la UI
    val id: Int,

    // Almacena el nombre del estudiante que sera renderizado en la fila de control
    val nombre: String,

    // Campo variable que sincroniza el estado del Checkbox en la pantalla de control de asistencia.
    // Al ser declarada con 'var', permite cambiar su valor de verdadero a falso de forma dinamica
    var llego: Boolean = false
)