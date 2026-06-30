package com.example.avancesproyecto.ui.theme.Screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.avancesproyecto.navigation.Routes
import com.example.avancesproyecto.ui.theme.VerdeOscuro
import com.example.avancesproyecto.viewmodel.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: EventViewModel
) {

    // Lista de eventos obtenida del ViewModel para asegurar la recomposición reactiva
    val events = viewModel.events

    Scaffold(
        floatingActionButton = {
            // Boton flotante para que los estudiantes propongan nuevos eventos
            FloatingActionButton(
                onClick = { navController.navigate(Routes.SUGGEST_EVENT) },
                containerColor = VerdeOscuro
            ) {
                Text("+", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("Green Events UAM") },
                actions = {
                    IconButton(
                        onClick = {
                            // Cierre de sesion seguro limpiando el historial de navegacion inmediato
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(Routes.HOME) { inclusive = true }
                            }
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Cerrar sesión",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VerdeOscuro,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->

        // Renderizado eficiente del feed de eventos disponibles
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            items(events, key = { it.id }) { event ->

                val isFull = event.attendees >= event.maxCapacity

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // Transicion hacia la pantalla de detalles del evento seleccionado
                            navController.navigate("${Routes.DETAIL}/${event.id}")
                        },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {

                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                            text = event.title,
                            style = MaterialTheme.typography.headlineSmall,
                            color = VerdeOscuro
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(event.description)

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Fecha: ${event.date}")
                            Text("Lugar: ${event.location}")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Asistentes: ${event.attendees} / ${event.maxCapacity}",
                            fontWeight = FontWeight.Bold,
                            color = VerdeOscuro
                        )

                        Text(
                            text = if (isFull) "Evento lleno" else "Cupos disponibles",
                            fontSize = 12.sp,
                            color = if (isFull) MaterialTheme.colorScheme.error else VerdeOscuro
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // ==========================================
                        // BOTON: REDIRECCION A FORMULARIO DE INSCRIPCION
                        // ==========================================
                        Button(
                            onClick = {
                                // Redirecciona de forma segura a la pantalla de inscripcion parametrizada
                                navController.navigate("${Routes.INSCRIPCION}/${event.id}")
                            },
                            enabled = !isFull,
                            modifier = Modifier.align(Alignment.End),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isFull)
                                    MaterialTheme.colorScheme.error
                                else
                                    VerdeOscuro
                            )
                        ) {
                            Text(if (isFull) "Completo" else "Inscribirse")
                        }
                    }
                }
            }
        }
    }
}