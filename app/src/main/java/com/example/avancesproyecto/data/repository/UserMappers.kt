package com.example.avancesproyecto.data.repository

import com.example.avancesproyecto.data.local.entity.UserEntity
import com.example.avancesproyecto.data.remote.UserDto
import com.example.avancesproyecto.model.User
import com.example.avancesproyecto.model.UserType

// =============================================================================
// MAPPERS PARA USUARIOS (Traducción de Datos)
// Con estas funciones de extensión logro que las capas de red, base de datos local
// y lógica de dominio intercambien datos de usuario sin acoplarse entre sí.
// =============================================================================

// Convierte un DTO de usuario proveniente de la API (Retrofit) en una entidad para Room.
// Asigno un 0 por defecto si el ID es nulo para que SQLite lo maneje de forma segura.
fun UserDto.toEntity(): UserEntity {
    return UserEntity(
        id = id ?: 0,
        name = name,
        cif = cif,
        userType = userType,
        isBlocked = blocked, // Sincronizo 'blocked' de la API con 'isBlocked' de mi tabla local
        registrationDate = registrationDate
    )
}

// Convierte la entidad de Room a mi modelo puro de dominio 'User'.
// Aquí hago la conversión del rol pasando de un String plano de la base de datos
// a nuestro objeto fuertemente tipado de tipo Enum (UserType).
fun UserEntity.toModel(): User {
    return User(
        id = id,
        name = name,
        cif = cif,
        userType = userType.toUserType(), // Invoco la función auxiliar de parseo seguro
        isBlocked = isBlocked,
        registrationDate = registrationDate
    )
}

// Convierte el modelo de la interfaz (User) a un DTO listo para ser serializado a JSON.
// Si el ID es 0, enviamos un 'null' para que la base de datos remota en la nube se encargue de autoincrementarlo.
fun User.toDto(): UserDto {
    return UserDto(
        id = if (id == 0) null else id,
        name = name,
        cif = cif,
        userType = userType.name, // Convertimos el valor del Enum a su representación en String
        blocked = isBlocked,
        registrationDate = registrationDate
    )
}

// Función de extensión privada para parsear de manera segura un String a nuestro Enum UserType.
// Si el backend llega a mandar un rol inesperado o corrupto, el 'catch' mitiga un posible crash
// en el celular devolviendo el rol por defecto: "ESTUDIANTE".
private fun String.toUserType(): UserType {
    return try {
        UserType.valueOf(this)
    } catch (e: Exception) {
        UserType.ESTUDIANTE
    }
}