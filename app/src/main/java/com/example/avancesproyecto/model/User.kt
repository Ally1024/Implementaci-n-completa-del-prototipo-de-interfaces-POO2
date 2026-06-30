package com.example.avancesproyecto.model

// Clase de datos principal para el control y gestion de usuarios en el sistema
data class User(
    // Identificador unico del usuario para su gestion en la persistencia de datos
    val id: Int,

    // Nombre completo o identificador publico de la persona
    val name: String,

    // Codigo de identificacion estudiantil (CIF) obligatorio para validar la matricula en la UAM
    val cif: String,

    // Rol asignado al usuario que por defecto se inicializa con el perfil de estudiante
    val userType: UserType = UserType.ESTUDIANTE,

    // Bandera de control para la seguridad que define si el acceso al sistema esta suspendido o no
    val isBlocked: Boolean = false,

    // Fecha y hora en la que se registro la cuenta por primera vez en la aplicacion
    val registrationDate: String = ""
)

// Enumerado de control rigido para definir los roles con acceso al sistema
enum class UserType {
    // Perfil con facultades completas de edicion, creacion de eventos y aprobacion de sugerencias
    ADMIN,

    // Perfil limitado para la consulta de eventos, inscripciones y envio de propuestas basicas
    ESTUDIANTE
}