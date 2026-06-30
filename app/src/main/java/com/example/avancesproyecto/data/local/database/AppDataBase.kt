package com.example.avancesproyecto.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.avancesproyecto.data.local.dao.AsistenciaDao
import com.example.avancesproyecto.data.local.dao.EventDao
import com.example.avancesproyecto.data.local.dao.UserDao
import com.example.avancesproyecto.data.local.dao.SuggestionDao
import com.example.avancesproyecto.data.local.entity.AsistenciaEntity
import com.example.avancesproyecto.data.local.entity.EventEntity
import com.example.avancesproyecto.data.local.entity.UserEntity
import com.example.avancesproyecto.data.local.entity.SuggestionEntity

// Defino la configuración de mi base de datos local SQLite usando Room
@Database(
    // Registro todas las tablas (entidades) del sistema incluyendo la de asistencia
    entities = [
        EventEntity::class,
        UserEntity::class,
        SuggestionEntity::class,
        AsistenciaEntity::class // Tabla de asistencia agregada correctamente aquí
    ],
    version = 4, // Incrementamos de 3 a 4 porque se agrego la nueva estructura de asistencia
    exportSchema = false // No exporto el esquema a un archivo JSON para mantener el proyecto mas ligero
)
abstract class AppDatabase : RoomDatabase() {

    // Expongo las funciones abstractas para acceder a los DAOs (los metodos que ejecutan las consultas)
    abstract fun eventDao(): EventDao
    abstract fun userDao(): UserDao
    abstract fun suggestionDao(): SuggestionDao
    abstract fun asistenciaDao(): AsistenciaDao // Acceso al DAO de asistencia registrado para el control global

    companion object {
        // @Volatile asegura que el valor de INSTANCE sea siempre visible y actualizado para todos los hilos del celular
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Aplico el patron Singleton para garantizar que exista una sola instancia de la base de datos abierta en la app
        fun getDatabase(context: Context): AppDatabase {
            // Si ya existe la base de datos la devuelvo; si no, la creo de manera segura usando un bloque synchronized
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "eventos_db_local" // El nombre fisico del archivo SQLite en el almacenamiento interno
                )
                    // Si cambio la version de la base de datos, destruye las tablas viejas y crea la nueva estructura.
                    // Esto evita que la app lance un crash por desajuste de columnas durante el desarrollo.
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}