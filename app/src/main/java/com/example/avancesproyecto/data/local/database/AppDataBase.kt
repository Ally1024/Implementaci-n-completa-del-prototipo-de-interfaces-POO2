package com.example.avancesproyecto.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.avancesproyecto.data.local.dao.EventDao
import com.example.avancesproyecto.data.local.entity.EventEntity
@Database(
    entities = [EventEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "eventos_db_local"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}