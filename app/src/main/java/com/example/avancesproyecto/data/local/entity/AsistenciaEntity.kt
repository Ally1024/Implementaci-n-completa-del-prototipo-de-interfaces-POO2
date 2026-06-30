package com.example.avancesproyecto.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Defino que esta clase de datos representa una tabla fisica llamada asistencia en SQLite
@Entity(tableName = "asistencia")
data class AsistenciaEntity(
    // Clave primaria autoincremental para llevar el control numerico unico de cada registro de asistencia
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    // Almacena el identificador unico del alumno (como su CIF o el nombre de usuario de inicio de sesion)
    val estudianteId: String,

    // Guarda el nombre completo del estudiante para mostrarlo directamente en la lista del administrador
    val nombreEstudiante: String,

    // Llave foranea logica que vincula de forma directa este registro con el evento correspondiente
    val eventoId: Int,

    // Campo booleano controlado por el Checkbox del administrador para verificar si el alumno asistio fisicamente
    val llego: Boolean = false
)