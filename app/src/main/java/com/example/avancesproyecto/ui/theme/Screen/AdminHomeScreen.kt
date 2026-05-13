package com.example.avancesproyecto.ui.theme.Screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.avancesproyecto.navigation.Routes
import com.example.avancesproyecto.ui.theme.VerdeOscuro
import com.example.avancesproyecto.ui.theme.White
import com.example.avancesproyecto.viewmodel.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(navController: NavHostController, onLogout: () -> Unit) {
    val adminName = SessionManager.currentAdmin?.name ?: "Administrador"

    Scaffold(
        topBar = {
            TopAppBar(
                title  = { Text("Panel Administrador") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor    = VerdeOscuro,
                    titleContentColor = White
                ),
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Salir 🚪", color = White)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Bienvenido, $adminName", fontSize = 18.sp, color = VerdeOscuro, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(20.dp))

            val opciones = listOf(
                Triple("➕", "Crear Evento",      Routes.ADMIN_CREAR_EVENTO),
                Triple("✅", "Asistencia",         Routes.ADMIN_ASISTENCIA),
                Triple("👥", "Gestionar Usuarios", Routes.ADMIN_USUARIOS),
                Triple("📊", "Base de Datos",      Routes.ADMIN_DASHBOARD)
            )

            LazyVerticalGrid(
                columns              = GridCells.Fixed(2),
                verticalArrangement  = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(opciones.size) { i ->
                    val (emoji, label, route) = opciones[i]
                    Card(
                        modifier  = Modifier.fillMaxWidth().height(130.dp)
                            .clickable { navController.navigate(route) },
                        shape     = RoundedCornerShape(18.dp),
                        colors    = CardDefaults.cardColors(containerColor = VerdeOscuro),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Column(
                            modifier            = Modifier.fillMaxSize().padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(emoji, fontSize = 40.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(label, color = White, fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}
