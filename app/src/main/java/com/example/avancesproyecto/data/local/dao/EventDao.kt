package com.example.avancesproyecto.data.local.dao

import androidx.room.*
import com.example.avancesproyecto.data.local.entity.EventEntity
import kotlinx.coroutines.flow.Flow

// Esta interfaz define el DAO para manejar todas las consultas locales sobre la tabla "events".
// Es la pieza fundamental de mi estrategia Offline-First: me permite persistir los eventos oficiales en el cel.
@Dao
interface EventDao {

    // Extraigo todos los eventos guardados localmente. Al retornar un 'Flow',
    // la interfaz gráfica se redibuja de inmediato en cuanto cambie cualquier dato o se limpie la caché.
    @Query("SELECT * FROM events")
    fun getEvents(): Flow<List<EventEntity>>

    // Inserta un bloque completo de eventos en la base de datos interna.
    // Lo uso para guardar masivamente en el celular todo lo que jalo de la API al actualizar.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)

    // Inserta o actualiza un evento individual en la caché local.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)

    // Elimina un evento de la base de datos local basándose en su ID.
    @Query("DELETE FROM events WHERE id = :id")
    suspend fun deleteEvent(id: Int)

    // Borra todos los registros de eventos de la base de datos del teléfono.
    // Ideal para limpiar la caché y forzar una sincronización limpia desde cero con el backend.
    @Query("DELETE FROM events")
    suspend fun clearEvents()
}