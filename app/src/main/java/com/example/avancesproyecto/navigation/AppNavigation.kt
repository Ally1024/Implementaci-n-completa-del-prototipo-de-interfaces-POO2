package com.example.avancesproyecto.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.avancesproyecto.data.local.database.AppDatabase
import com.example.avancesproyecto.ui.theme.Screen.*
import com.example.avancesproyecto.viewmodel.EventViewModel
import com.example.avancesproyecto.viewmodel.SuggestionViewModel
import com.example.avancesproyecto.viewmodel.UserViewModel

@Composable
fun AppNavigation() {

    // Inicializo el controlador central de navegacion de Jetpack Compose
    val navController = rememberNavController()

    // Instancio los ViewModels globales para retener el estado de la aplicacion en los cambios de pantalla
    val eventViewModel: EventViewModel = viewModel()
    val suggestionViewModel: SuggestionViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {

        // Pantalla de autenticacion inicial
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginClick = { isAdmin ->
                    // Redireccion condicional basada en el rol del usuario autenticado
                    if (isAdmin) {
                        navController.navigate(Routes.ADMIN) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                },
                onRegisterClick = {
                    navController.navigate("register")
                }
            )
        }

        // Pantalla de registro de nuevas cuentas de estudiante
        composable("register") {
            RegisterScreen(
                viewModel = userViewModel,
                onRegisterClick = {
                    navController.popBackStack()
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // Panel principal o Home para la vista del estudiante
        composable(Routes.HOME) {
            HomeScreen(
                navController = navController,
                viewModel = eventViewModel
            )
        }

        // Panel de control central para los administradores
        composable(Routes.ADMIN) {
            AdminPanelScreen(
                navController = navController,
                viewModel = eventViewModel,
                suggestionViewModel = suggestionViewModel
            )
        }

        // Pantalla para la creacion de nuevos eventos por parte del administrador
        composable(Routes.ADD_EVENT) {
            AddEventScreen(
                navController = navController,
                viewModel = eventViewModel
            )
        }

        // Pantalla para la eliminacion masiva o selectiva de eventos
        composable(Routes.DELETE_EVENT) {
            DeleteEventScreen(
                viewModel = eventViewModel,
                navController = navController
            )
        }

        // Pantalla para que los estudiantes envien propuestas de eventos ecologicos
        composable(Routes.SUGGEST_EVENT) {
            SuggestEventScreen(
                navController = navController,
                suggestionViewModel = suggestionViewModel
            )
        }

        // Pantalla de detalle de un evento que extrae el ID desde los argumentos de la ruta
        composable("${Routes.DETAIL}/{eventId}") { backStack ->
            val id = backStack.arguments
                ?.getString("eventId")
                ?.toInt() ?: 0

            DetailScreen(
                id = id,
                viewModel = eventViewModel,
                navController = navController
            )
        }

        // Pantalla para visualizar los eventos a los que se ha inscrito el usuario actual
        composable(Routes.REGISTERED) {
            RegisteredScreen(
                viewModel = eventViewModel,
                navController = navController
            )
        }

        // Formulario de inscripcion parametrizado con el ID del evento seleccionado
        composable("${Routes.INSCRIPCION}/{eventId}") { backStack ->
            val id = backStack.arguments
                ?.getString("eventId")
                ?.toInt() ?: 0

            InscripcionScreen(
                navController = navController,
                viewModel = eventViewModel,
                eventId = id
            )
        }

        // Pantalla de edicion de eventos para administradores guiada por el ID del evento
        composable("${Routes.EDIT_EVENT}/{eventId}") { backStack ->
            val id = backStack.arguments
                ?.getString("eventId")
                ?.toInt() ?: 0

            EditEventScreen(
                eventId = id,
                navController = navController,
                viewModel = eventViewModel
            )
        }

        // Pantalla para la gestion y baneo/bloqueo de usuarios del sistema
        composable(Routes.USERS_MANAGEMENT) {
            UsersManagementScreen(
                viewModel = userViewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Pantalla de control de asistencia fisica (Check-In) mediante lectura de persistencia local
        composable("check_in_screen") {
            val context = LocalContext.current

            // Instanciacion directa de DAOs desde el Singleton de Room para la pantalla de control
            val database = AppDatabase.getDatabase(context)
            val asistenciaDao = database.asistenciaDao()
            val eventDao = database.eventDao()

            CheckInScreen(
                asistenciaDao = asistenciaDao,
                eventDao = eventDao,
                navController = navController
            )
        }
    }
}