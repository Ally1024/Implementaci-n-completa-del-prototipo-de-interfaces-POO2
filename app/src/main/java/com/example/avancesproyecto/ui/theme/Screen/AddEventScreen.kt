package com.example.avancesproyecto.ui.theme.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.avancesproyecto.navigation.Routes
import com.example.avancesproyecto.ui.theme.VerdeOscuro

// Redirige al admin - se mantiene por compatibilidad de navegación
@Composable
fun AddEventScreen(navController: NavHostController) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = { navController.navigate(Routes.ADMIN_CREAR_EVENTO) },
            colors = ButtonDefaults.buttonColors(containerColor = VerdeOscuro)) {
            Text("Ir a Crear Evento (Admin)")
        }
    }
}
