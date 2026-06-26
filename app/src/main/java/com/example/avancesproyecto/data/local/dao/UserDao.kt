package com.example.avancesproyecto.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.avancesproyecto.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

// Esta interfaz define el DAO (Data Access Object) para interactuar con la tabla de usuarios local.
// Aquí configuro las consultas SQL directas que la base de datos de Room va a ejecutar.
@Dao
interface UserDao {

    // Devuelvo la lista de usuarios envuelta en un 'Flow'.
    // Uso Flow de Kotlin para que la vista se mantenga escuchando activamente la base de datos;
    // si un usuario se bloquea o elimina de forma local, la pantalla se actualiza en automático sin recargar.
    @Query("SELECT * FROM users")
    fun getUsers(): Flow<List<UserEntity>>

    // Inserta una lista completa de usuarios (útil para guardar en caché lo que viene de la API).
    // Si un usuario ya existe con el mismo ID, 'REPLACE' sobreescribe sus datos con los más nuevos.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    // Inserta o actualiza un único usuario en la base de datos local de forma asíncrona (suspend).
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    // Elimina un usuario específico de la base de datos local usando su ID como parámetro.
    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUser(id: Int)

    // Limpia por completo la tabla de usuarios en caché local.
    // Ideal si quiero refrescar la base de datos limpia desde el servidor.
    @Query("DELETE FROM users")
    suspend fun clearUsers()
}