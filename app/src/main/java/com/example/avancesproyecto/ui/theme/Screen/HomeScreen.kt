package com.example.avancesproyecto.ui.theme.Screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.avancesproyecto.viewmodel.EventViewModel

// HomeScreen reemplazado por AlumnoHomeScreen en la navegación.
// Se mantiene este archivo para compatibilidad de compilación.
@Composable
fun HomeScreen(navController: NavHostController, viewModel: EventViewModel) {
    AlumnoHomeScreen(
        navController  = navController,
        eventViewModel = viewModel,
        onLogout       = { navController.popBackStack() }
    )
}
