package com.example.avancesproyecto.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.avancesproyecto.ui.theme.Screen.*
import com.example.avancesproyecto.viewmodel.AuthViewModel
import com.example.avancesproyecto.viewmodel.EventViewModel
import com.example.avancesproyecto.viewmodel.SessionManager

@Composable
fun AppNavigation() {
    val navController  = rememberNavController()
    val eventViewModel : EventViewModel = viewModel()
    val authViewModel  : AuthViewModel  = viewModel()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {

        // ── LOGIN ─────────────────────────────────────────
        composable(Routes.LOGIN) {
            LoginScreen(
                authViewModel = authViewModel,
                onAdminLogin  = {
                    navController.navigate(Routes.ADMIN_HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onUserLogin = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // ── ADMIN ─────────────────────────────────────────
        composable(Routes.ADMIN_HOME) {
            AdminHomeScreen(
                navController  = navController,
                onLogout       = {
                    SessionManager.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.ADMIN_HOME) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.ADMIN_CREAR_EVENTO) {
            AdminCrearEventoScreen(navController, eventViewModel)
        }

        composable(Routes.ADMIN_USUARIOS) {
            AdminUsuariosScreen(navController, authViewModel)
        }

        composable(Routes.ADMIN_ASISTENCIA) {
            AdminAsistenciaScreen(navController, eventViewModel, authViewModel)
        }

        composable(Routes.ADMIN_DASHBOARD) {
            AdminDashboardScreen(navController, eventViewModel, authViewModel)
        }

        // ── ALUMNO ────────────────────────────────────────
        composable(Routes.HOME) {
            AlumnoHomeScreen(
                navController  = navController,
                eventViewModel = eventViewModel,
                onLogout       = {
                    SessionManager.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }

        composable("${Routes.DETAIL}/{eventId}") { back: NavBackStackEntry ->
            val id = back.arguments?.getString("eventId")?.toInt() ?: 0
            DetailScreen(id, eventViewModel, navController)
        }

        composable(Routes.REGISTERED) {
            RegisteredScreen(eventViewModel, navController)
        }

        composable(Routes.INSCRIPCION) {
            InscripcionScreen(navController)
        }

        composable(Routes.PERFIL) {
            AlumnoPerfilScreen(navController, authViewModel, eventViewModel)
        }
    }
}
