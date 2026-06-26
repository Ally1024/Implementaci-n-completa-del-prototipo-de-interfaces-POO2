package com.example.avancesproyecto.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.avancesproyecto.model.UserType

// Defino la tabla "users" para el almacenamiento interno de SQLite mediante Room.
// Esto me permite guardar los perfiles de los usuarios de forma local en el teléfono.
@Entity(tableName = "users")
data class UserEntity(
    // Llave primaria fija e ID obligatorio. Al ser datos sincronizados desde el servidor,
    // ya vienen con su identificador único asignado desde Supabase.
    @PrimaryKey val id: Int,

    // Columnas que representan la información de la cuenta localmente
    val name: String,
    val cif: String,

    // Almaceno el rol como String directo en SQLite para simplificar la persistencia sin TypeConverters,
    // pero importo UserType por si necesito mapear o validar contra el enum en otras partes de la app.
    val userType: String = "ESTUDIANTE",

    val isBlocked: Boolean = false,    // Refleja de forma local si el usuario está baneado o no
    val registrationDate: String = "" // Fecha en que se unió el usuario al sistema
)