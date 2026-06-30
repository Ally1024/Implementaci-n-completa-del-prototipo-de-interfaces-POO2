package com.example.avancesproyecto.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avancesproyecto.data.local.dao.AsistenciaDao
import com.example.avancesproyecto.data.local.entity.AsistenciaEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AsistenciaViewModel(private val asistenciaDao: AsistenciaDao) : ViewModel() {

    // Obtiene el flujo de asistencia en tiempo real mapeando el Flow de Room a StateFlow optimizado para Compose
    fun getAsistencia(eventoId: Int): StateFlow<List<AsistenciaEntity>> {
        return asistenciaDao.getAsistenciaPorEvento(eventoId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    // Actualiza de forma asincrona el estado de confirmacion de asistencia en la base de datos local
    fun cambiarEstadoAsistencia(asistencia: AsistenciaEntity, llego: Boolean) {
        viewModelScope.launch {
            asistenciaDao.actualizarAsistencia(asistencia.copy(llego = llego))
        }
    }
}