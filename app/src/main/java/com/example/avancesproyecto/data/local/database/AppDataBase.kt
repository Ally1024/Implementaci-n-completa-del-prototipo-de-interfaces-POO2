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
    // Registro todas las tablas (entidades) incluyendo la nueva de asistencia
    entities = [
        EventEntity::class,
        UserEntity::class,
        SuggestionEntity::class,
        AsistenciaEntity::class // 👈 Agregada correctamente aquí
    ],
    version = 4, // 👈 Incrementamos de 3 a 4 porque agregamos una nueva tabla
    exportSchema = false // No exporto el esquema a un archivo JSON para mantener el proyecto más ligero
)
abstract class AppDatabase : RoomDatabase() {

    // Expongo las funciones abstractas para acceder a los DAOs (los métodos que hacen las consultas)
    abstract fun eventDao(): EventDao
    abstract fun userDao(): UserDao
    abstract fun suggestionDao(): SuggestionDao
    abstract fun asistenciaDao(): AsistenciaDao // 👈 Agregado el acceso al DAO de asistencia

    companion object {
        // @Volatile asegura que el valor de INSTANCE sea siempre visible y actualizado para todos los hilos del cel
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Aplico el patrón Singleton para garantizar que solo exista una sola instancia de la base de datos local abierta
        fun getDatabase(context: Context): AppDatabase {
            // Si ya existe la base de datos la devuelvo; si no, la creo de manera segura usando synchronized
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "eventos_db_local" // El nombre físico del archivo SQLite en el almacenamiento interno
                )
                    // Si cambio la versión de la base de datos, borra las tablas viejas y crea la nueva estructura
                    // (Evita que la app me tire un crash por desajustes de columnas)
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}