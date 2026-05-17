package com.example.avancesproyecto.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.avancesproyecto.ui.theme.Screen.AddEventScreen
import com.example.avancesproyecto.ui.theme.Screen.AdminPanelScreen
import com.example.avancesproyecto.ui.theme.Screen.DetailScreen
import com.example.avancesproyecto.ui.theme.Screen.HomeScreen
import com.example.avancesproyecto.ui.theme.Screen.InscripcionScreen
import com.example.avancesproyecto.ui.theme.Screen.LoginScreen
import com.example.avancesproyecto.ui.theme.Screen.RegisterScreen
import com.example.avancesproyecto.ui.theme.Screen.RegisteredScreen
import com.example.avancesproyecto.viewmodel.EventViewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val viewModel: EventViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {

        // LOGIN
        composable(Routes.LOGIN) {

            LoginScreen(

                onLoginClick = { isAdmin ->

                    if (isAdmin) {

                        navController.navigate(
                            Routes.ADMIN
                        ) {

                            popUpTo(Routes.LOGIN) {
                                inclusive = true
                            }
                        }

                    } else {

                        navController.navigate(
                            Routes.HOME
                        ) {

                            popUpTo(Routes.LOGIN) {
                                inclusive = true
                            }
                        }
                    }
                },

                onRegisterClick = {

                    navController.navigate(
                        "register"
                    )
                }
            )
        }

        // REGISTER
        composable("register") {

            RegisterScreen(

                onRegisterClick = {

                    navController.popBackStack()
                },

                onBackToLogin = {

                    navController.popBackStack()
                }
            )
        }

        // HOME
        composable(Routes.HOME) {

            HomeScreen(
                navController,
                viewModel
            )
        }

        // ADMIN
        composable(Routes.ADMIN) {

            AdminPanelScreen(
                navController,
                viewModel
            )
        }

        // DETAIL
        composable(
            "${Routes.DETAIL}/{eventId}"
        ) { backStack: NavBackStackEntry ->

            val id =
                backStack.arguments
                    ?.getString("eventId")
                    ?.toInt() ?: 0

            DetailScreen(
                id,
                viewModel,
                navController
            )
        }

        // REGISTERED
        composable(Routes.REGISTERED) {

            RegisteredScreen(
                viewModel,
                navController
            )
        }

        // INSCRIPCION
        composable(Routes.INSCRIPCION) {

            InscripcionScreen(
                navController
            )
        }

        // ADD EVENT
        composable(Routes.ADD_EVENT) {

            AddEventScreen(
                navController
            )
        }
    }
}