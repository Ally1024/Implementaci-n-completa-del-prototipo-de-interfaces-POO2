package com.example.avancesproyecto.data.repository

import com.example.avancesproyecto.data.local.entity.UserEntity
import com.example.avancesproyecto.data.remote.UserDto
import com.example.avancesproyecto.model.User
import com.example.avancesproyecto.model.UserType

fun UserDto.toEntity(): UserEntity {
    return UserEntity(
        id = id ?: 0,
        name = name,
        cif = cif,
        userType = userType,
        isBlocked = blocked,
        registrationDate = registrationDate
    )
}

fun UserEntity.toModel(): User {
    return User(
        id = id,
        name = name,
        cif = cif,
        userType = userType.toUserType(),
        isBlocked = isBlocked,
        registrationDate = registrationDate
    )
}

fun User.toDto(): UserDto {
    return UserDto(
        id = if (id == 0) null else id,
        name = name,
        cif = cif,
        userType = userType.name,
        blocked = isBlocked,
        registrationDate = registrationDate
    )
}

private fun String.toUserType(): UserType {
    return try {
        UserType.valueOf(this)
    } catch (e: Exception) {
        UserType.ESTUDIANTE
    }
}