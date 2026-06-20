package com.example.avancesproyecto.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.avancesproyecto.data.local.dao.EventDao
import com.example.avancesproyecto.data.local.dao.UserDao
import com.example.avancesproyecto.data.local.entity.EventEntity
import com.example.avancesproyecto.data.local.entity.UserEntity
import com.example.avancesproyecto.data.local.entity.SuggestionEntity
import com.example.avancesproyecto.data.local.dao.SuggestionDao

@Database(
    entities = [
        EventEntity::class,
        UserEntity::class,
        SuggestionEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao
    abstract fun userDao(): UserDao
    abstract fun suggestionDao(): SuggestionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "eventos_db_local"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}