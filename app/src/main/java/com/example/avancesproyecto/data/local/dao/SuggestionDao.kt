package com.example.avancesproyecto.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.avancesproyecto.data.local.entity.SuggestionEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface SuggestionDao {

    @Query("SELECT * FROM suggestions")
    fun getSuggestions(): Flow<List<SuggestionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuggestions(
        suggestions: List<SuggestionEntity>
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuggestion(
        suggestion: SuggestionEntity
    )

    @Query("DELETE FROM suggestions WHERE id = :id")
    suspend fun deleteSuggestion(id: Int)
}