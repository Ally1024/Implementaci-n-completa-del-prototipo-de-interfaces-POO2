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

    private val _users = mutableStateListOf<User>()

    val users: List<User>
        get() = _users

    // CAMBIO: Estado para guardar el nombre del usuario logueado en la sesión actual
    var loggedUserName by mutableStateOf<String?>(null)
        private set

    // ✨ NUEVO CAMBIO: Estado para almacenar y mostrar errores de duplicados en la pantalla de Registro
    var registrationError by mutableStateOf<String?>(null)

    init {
        observeLocalUsers()
        refreshUsers()
    }

    private fun observeLocalUsers() {
        viewModelScope.launch {
            userRepository.users.collect { userList ->
                _users.clear()
                _users.addAll(userList)
            }
        }
    }

    fun refreshUsers() {
        viewModelScope.launch {
            try {
                userRepository.syncUsers()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // CAMBIO: Función para simular el inicio de sesión y guardar el nombre en memoria
    fun loginUser(cifOrName: String): User? {
        val foundUser = _users.find {
            it.cif.equals(cifOrName, ignoreCase = true) ||
                    it.name.equals(cifOrName, ignoreCase = true)
        }

        if (foundUser != null && !foundUser.isBlocked) {
            loggedUserName = foundUser.name
        }
        return foundUser
    }

    // 🛠️ MODIFICADO: Ahora maneja las respuestas de error del backend por duplicado
    fun addUser(
        name: String,
        cif: String,
        userType: UserType = UserType.ESTUDIANTE,
        onSuccess: () -> Unit // Agregamos un callback para avisar a la pantalla que cierre si todo sale bien
    ) {
        // Limpiamos errores anteriores antes de intentar registrar
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
                // Llamamos al repositorio pasándole el usuario
                val response = userRepository.addUser(newUser)

                if (response.isSuccessful) {
                    // ✅ Éxito total en el servidor
                    refreshUsers()
                    onSuccess() // Redirige al Login o cierra el formulario
                } else {
                    // ❌ El Backend nos regresó un error 400 (Duplicado)
                    val errorMsg = response.errorBody()?.string() ?: "Error al registrar usuario"
                    registrationError = errorMsg
                }
            } catch (e: Exception) {
                e.printStackTrace()
                registrationError = "Error de conexión: No se pudo conectar al servidor."
            }
        }
    }

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

    fun searchUsers(query: String): List<User> {
        if (query.isEmpty()) return _users

        return _users.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.cif.contains(query, ignoreCase = true)
        }
    }

    private fun getCurrentDate(): String {
        val calendar = java.util.Calendar.getInstance()
        val year = calendar.get(java.util.Calendar.YEAR)
        val month = calendar.get(java.util.Calendar.MONTH) + 1
        val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)

        return String.format("%04d-%02d-%02d", year, month, day)
    }
}