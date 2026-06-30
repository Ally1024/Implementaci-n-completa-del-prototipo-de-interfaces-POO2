package com.example.avancesproyecto.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.avancesproyecto.data.local.entity.AsistenciaEntity
import kotlinx.coroutines.flow.Flow

// Defino que esta interfaz es mi DAO para que Room sepa que aquí controlo la tabla de asistencia
@Dao
interface AsistenciaDao {

    // ==========================================
    // CONSULTA: OBTENER ASISTENCIA POR EVENTO
    // ==========================================
    // Busco en la tabla todos los alumnos inscritos que correspondan al ID del evento seleccionado.
    // Uso 'Flow' para que la lista sea reactiva en tiempo real; si un alumno se registra o cambia
    // su estado, la pantalla de Check-In se repinta automáticamente sin recargar.
    @Query("SELECT * FROM asistencia WHERE eventoId = :eventoId")
    fun getAsistenciaPorEvento(eventoId: Int): Flow<List<AsistenciaEntity>>

    // ==========================================
    // OPERACIÓN: REGISTRAR NUEVO ASISTENTE
    // ==========================================
    // Inserta una fila nueva cuando un estudiante se inscribe desde el formulario de la app.
    // Uso 'OnConflictStrategy.REPLACE' por seguridad: si por algún motivo se intenta meter un duplicado
    // del mismo estudiante en el mismo evento, sobrescribe el registro viejo en vez de romper la app.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarAsistencia(asistencia: AsistenciaEntity)

    // ==========================================
    // OPERACIÓN: ACTUALIZAR CHECKBOX DE ASISTENCIA
    // ==========================================
    // Esta función es vital para la pantalla de 'CheckInScreen'. Cuando el administrador toca
    // el Checkbox (marcando o desmarcando si el alumno llegó físicamente al evento), esta operación
    // actualiza el campo booleano directamente en la base de datos local de Room.
    @Update
    suspend fun actualizarAsistencia(asistencia: AsistenciaEntity)
}