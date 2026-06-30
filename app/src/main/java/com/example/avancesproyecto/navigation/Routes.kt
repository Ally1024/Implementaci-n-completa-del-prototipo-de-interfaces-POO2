package com.example.avancesproyecto.navigation

// Objeto global que centraliza las cadenas de texto para las rutas de navegacion
object Routes {
    // Identificador de la pantalla de inicio de sesion
    const val LOGIN = "login"

    // Identificador de la pantalla principal para estudiantes
    const val HOME = "home"

    // Ruta base para la pantalla de informacion detallada de un evento
    const val DETAIL = "detail"

    // Identificador de la pantalla de eventos en los que el usuario ya se inscribio
    const val REGISTERED = "registered"

    // Ruta base para el formulario donde el estudiante introduce sus datos de inscripcion
    const val INSCRIPCION = "inscripcion"

    // Identificador de la pantalla para proponer nuevos eventos ambientales
    const val SUGGEST_EVENT = "suggest_event"

    // Identificador de la pantalla de creacion de eventos para administradores
    const val ADD_EVENT ="add_event"

    // Identificador de la pantalla para la eliminacion de eventos activos
    const val DELETE_EVENT ="delete_event"

    // Identificador del panel de control principal del administrador
    const val ADMIN = "admin"

    // Ruta base para la pantalla que permite modificar la informacion de un evento existente
    const val EDIT_EVENT = "edit_event"

    // Identificador de la pantalla de administracion y control de usuarios de la aplicacion
    const val USERS_MANAGEMENT = "users_management"
}