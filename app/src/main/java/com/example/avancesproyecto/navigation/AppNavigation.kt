package com.example.avancesproyecto.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.avancesproyecto.ui.theme.Screen.*
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

                        navController.navigate(Routes.ADMIN) {

                            popUpTo(Routes.LOGIN) {
                                inclusive = true
                            }
                        }

                    } else {

                        navController.navigate(Routes.HOME) {

                            popUpTo(Routes.LOGIN) {
                                inclusive = true
                            }
                        }
                    }
                },

                onRegisterClick = {

                    navController.navigate("register")
                }
            )
        }

        // REGISTER
        composable("register") {

            RegisterScreen(
                viewModel = viewModel,
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
                navController = navController,
                viewModel = viewModel
            )
        }

        // ADMIN PANEL
        composable(Routes.ADMIN) {

            AdminPanelScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        // ADD EVENT (ADMIN)
        composable(Routes.ADD_EVENT) {

            AddEventScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        // DELETE EVENT
        composable(Routes.DELETE_EVENT) {

            DeleteEventScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        // SUGGEST EVENT (ESTUDIANTE)
        composable(Routes.SUGGEST_EVENT) {

            SuggestEventScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        // DETAIL
        composable(
            "${Routes.DETAIL}/{eventId}"
        ) { backStack: NavBackStackEntry ->

            val id = backStack.arguments
                ?.getString("eventId")
                ?.toInt() ?: 0

            DetailScreen(
                id = id,
                viewModel = viewModel,
                navController = navController
            )
        }

        // REGISTERED EVENTS
        composable(Routes.REGISTERED) {

            RegisteredScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        // INSCRIPCION
        composable(Routes.INSCRIPCION) {

            InscripcionScreen(
                navController = navController
            )
        }

        composable(
            "${Routes.EDIT_EVENT}/{eventId}"
        ) { backStack ->

            val id =
                backStack.arguments
                    ?.getString("eventId")
                    ?.toInt() ?: 0

            EditEventScreen(

                eventId = id,

                navController = navController,

                viewModel = viewModel
            )
        }

        // USERS MANAGEMENT
        composable(Routes.USERS_MANAGEMENT) {

            UsersManagementScreen(
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}