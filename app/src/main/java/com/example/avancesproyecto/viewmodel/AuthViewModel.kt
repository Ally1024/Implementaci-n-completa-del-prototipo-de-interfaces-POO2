package com.example.avancesproyecto.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.avancesproyecto.model.Admin
import com.example.avancesproyecto.model.User

class AuthViewModel : ViewModel() {

    // Admin: CIF solo numeros, sin limite de digitos
    private val admin = Admin("12345678901", "Administrador UAM", "admin123")

    // Alumnos: CIF exactamente 8 digitos numericos
    private val _users = mutableStateListOf(
        User(1, "Ana López",    "20240001", "Ingeniería en Sistemas",     "pass123"),
        User(2, "Carlos Mejía", "20240002", "Administración de Empresas", "pass456"),
        User(3, "María García", "20240003", "Derecho",                    "pass789")
    )
    val users: List<User> get() = _users
    private var nextId = 4

    // ── LOGIN ──────────────────────────────────────────
    fun loginAdmin(cif: String, password: String): Boolean {
        return if (cif.trim() == admin.cif && password == admin.password) {
            SessionManager.currentAdmin = admin
            SessionManager.currentUser  = null
            true
        } else false
    }

    fun loginUser(cif: String, password: String): Boolean {
        val user = _users.find { it.cif == cif.trim() && it.password == password }
        return if (user != null) {
            SessionManager.currentUser  = user
            SessionManager.currentAdmin = null
            true
        } else false
    }

    // ── CRUD USUARIOS ──────────────────────────────────
    fun addUser(name: String, cif: String, career: String, password: String) {
        _users.add(User(nextId++, name, cif.trim(), career, password))
    }

    fun deleteUser(id: Int) {
        _users.removeAll { it.id == id }
    }

    fun changePassword(userId: Int, newPassword: String) {
        val idx = _users.indexOfFirst { it.id == userId }
        if (idx >= 0) {
            _users[idx] = _users[idx].copy(password = newPassword)
            if (SessionManager.currentUser?.id == userId)
                SessionManager.currentUser = _users[idx]
        }
    }

    fun getUserById(id: Int): User? = _users.find { it.id == id }
}
