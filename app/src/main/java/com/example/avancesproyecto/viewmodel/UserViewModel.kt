package com.example.avancesproyecto.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.avancesproyecto.data.local.database.AppDatabase
import com.example.avancesproyecto.data.remote.RetrofitClient
import com.example.avancesproyecto.data.repository.UserRepository
import com.example.avancesproyecto.model.User
import com.example.avancesproyecto.model.UserType
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)

    private val userRepository = UserRepository(
        api = RetrofitClient.userApi,
        dao = database.userDao()
    )

    // Estado interno mutable optimizado para el manejo de colecciones observables en Compose
    private val _users = mutableStateListOf<User>()
    val users: List<User> get() = _users

    // Estado observable para realizar el seguimiento del usuario logueado en la sesion activa
    var loggedUserName by mutableStateOf<String?>(null)
        private set

    // Estado para capturar y renderizar excepciones o conflictos por duplicado desde el backend
    var registrationError by mutableStateOf<String?>(null)

    init {
        observeLocalUsers()
        refreshUsers()
    }

    // Suscripcion reactiva al flujo de datos local expuesto por Room
    private fun observeLocalUsers() {
        viewModelScope.launch {
            userRepository.users.collect { userList ->
                _users.clear()
                _users.addAll(userList)
            }
        }
    }

    // Sincroniza la coleccion local mediante peticiones directas a la API REST
    fun refreshUsers() {
        viewModelScope.launch {
            try {
                userRepository.syncUsers()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Valida las credenciales en memoria local y asigna el identificador de sesion si procede
    fun loginUser(cifOrName: String): User? {
        val foundUser = _users.find { user ->
            user.cif.equals(cifOrName, ignoreCase = true) ||
                    user.name.equals(cifOrName, ignoreCase = true)
        }

        if (foundUser != null && !foundUser.isBlocked) {
            loggedUserName = foundUser.name
        }
        return foundUser
    }

    // Despacha la solicitud de insercion manejando codigos de respuesta Http e hilos asincronos
    fun addUser(
        name: String,
        cif: String,
        userType: UserType = UserType.ESTUDIANTE,
        onSuccess: () -> Unit
    ) {
        // Limpieza de estados de error previos al inicio de la transaccion
        registrationError = null

        val newUser = User(
            id = 0,
            name = name,
            cif = cif,
            userType = userType,
            isBlocked = false,
            registrationDate = getCurrentDate()
        )

        viewModelScope.launch {
            try {
                val response = userRepository.addUser(newUser)

                if (response.isSuccessful) {
                    // Confirmacion exitosa por parte del servidor central
                    refreshUsers()
                    onSuccess()
                } else {
                    // Manejo alternativo ante respuestas del tipo 400 Bad Request o duplicados
                    val errorMsg = response.errorBody()?.string() ?: "Error al registrar usuario"
                    registrationError = errorMsg
                }
            } catch (e: Exception) {
                e.printStackTrace()
                registrationError = "Error de conexión: No se pudo conectar al servidor."
            }
        }
    }

    // Elimina de forma logica o fisica el registro correspondiente a un identificador unico
    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            try {
                userRepository.deleteUser(userId)
                refreshUsers()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Invierte el estado de bloqueo de un usuario para restringir o permitir su acceso
    fun toggleBlockUser(userId: Int) {
        viewModelScope.launch {
            try {
                userRepository.toggleBlockUser(userId)
                refreshUsers()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getUser(userId: Int): User? {
        return _users.find { it.id == userId }
    }

    fun countActiveUsers(): Int {
        return _users.count { !it.isBlocked }
    }

    fun countBlockedUsers(): Int {
        return _users.count { it.isBlocked }
    }

    fun countUsersByType(userType: UserType): Int {
        return _users.count { it.userType == userType }
    }

    // Filtra concurrentemente la coleccion en base a la entrada de texto del administrador
    fun searchUsers(query: String): List<User> {
        if (query.isEmpty()) return _users

        return _users.filter { user ->
            user.name.contains(query, ignoreCase = true) ||
                    user.cif.contains(query, ignoreCase = true)
        }
    }

    // Genera una cadena formateada bajo la representacion estandar de fechas ISO-8601
    private fun getCurrentDate(): String {
        val calendar = java.util.Calendar.getInstance()
        val year = calendar.get(java.util.Calendar.YEAR)
        val month = calendar.get(java.util.Calendar.MONTH) + 1
        val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)

        return String.format("%04d-%02d-%02d", year, month, day)
    }
}