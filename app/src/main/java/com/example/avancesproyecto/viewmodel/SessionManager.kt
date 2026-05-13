package com.example.avancesproyecto.viewmodel

import com.example.avancesproyecto.model.Admin
import com.example.avancesproyecto.model.User

object SessionManager {
    var currentUser: User?  = null
    var currentAdmin: Admin? = null

    val isAdmin: Boolean get() = currentAdmin != null
    val isLoggedIn: Boolean get() = currentUser != null || currentAdmin != null

    fun logout() {
        currentUser  = null
        currentAdmin = null
    }
}
