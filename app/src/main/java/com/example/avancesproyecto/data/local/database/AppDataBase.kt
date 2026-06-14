package com.example.avancesproyecto.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.avancesproyecto.data.local.dao.EventDao
import com.example.avancesproyecto.data.local.dao.UserDao
import com.example.avancesproyecto.data.local.entity.EventEntity
import com.example.avancesproyecto.data.local.entity.UserEntity

@Database(
    entities = [
        EventEntity::class,
        UserEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao
    abstract fun userDao(): UserDao

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