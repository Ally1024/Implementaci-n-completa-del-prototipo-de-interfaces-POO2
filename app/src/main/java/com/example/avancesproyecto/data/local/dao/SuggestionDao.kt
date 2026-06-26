package com.example.avancesproyecto.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.avancesproyecto.data.local.entity.SuggestionEntity
import kotlinx.coroutines.flow.Flow

// Esta interfaz define el DAO para controlar el almacenamiento local de las sugerencias en SQLite.
// Me permite cachear las propuestas para que sigan viéndose en la app aunque se caiga la red.
@Dao
interface SuggestionDao {

    // Devuelvo el flujo reactivo de las sugerencias locales de forma continua (Flow).
    // Si borro o apruebo una sugerencia desde el panel, la lista en pantalla se actualiza solita.
    @Query("SELECT * FROM suggestions")
    fun getSuggestions(): Flow<List<SuggestionEntity>>

    // Guarda una lista completa de sugerencias en el almacenamiento local de un solo golpe.
    // Si ya existen por ID, sobreescribe las viejas con los datos frescos del servidor (REPLACE).
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuggestions(
        suggestions: List<SuggestionEntity>
    )

    // Almacena una única sugerencia. Útil cuando el estudiante crea una propuesta y la guardo localmente.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuggestion(
        suggestion: SuggestionEntity
    )

    // Elimina una sugerencia de la caché local por su ID (ej: cuando el admin la rechaza o aprueba).
    @Query("DELETE FROM suggestions WHERE id = :id")
    suspend fun deleteSuggestion(id: Int)
}