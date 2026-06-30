package com.example.avancesproyecto.ui.theme.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.avancesproyecto.navigation.Routes
import com.example.avancesproyecto.ui.theme.VerdeOscuro
import com.example.avancesproyecto.ui.theme.White
import com.example.avancesproyecto.viewmodel.EventViewModel
import com.example.avancesproyecto.viewmodel.SuggestionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    navController: NavHostController,
    viewModel: EventViewModel,
    suggestionViewModel: SuggestionViewModel
) {

    // =========================
    // ESTADISTICAS
    // =========================
    val totalEvents = viewModel.events.size
    val totalAttendees = viewModel.events.sumOf { it.attendees }
    val featuredEvents = viewModel.events.count { it.isFeatured }

    // =========================
    // BUSCADOR
    // =========================
    var search by remember { mutableStateOf("") }
    val filteredEvents = viewModel.events.filter {
        it.title.contains(search, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel Administrador") },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(Routes.ADMIN) { inclusive = true }
                            }
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Cerrar sesión",
                            tint = White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VerdeOscuro,
                    titleContentColor = White
                )
            )
        }
    ) { padding ->

        // Cambiamos el Column exterior por la LazyColumn directa con paddings correctos
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            // =========================================================
            // SECCIÓN ESTÁTICA SUPERIOR (Botones, Buscador y Tarjeta)
            // =========================================================
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Gestión de Eventos",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdeOscuro
                )
                Spacer(modifier = Modifier.height(20.dp))

                // BOTON AGREGAR
                Button(
                    onClick = { navController.navigate(Routes.ADD_EVENT) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeOscuro)
                ) {
                    Text("Agregar Evento")
                }
                Spacer(modifier = Modifier.height(2.dp))

                // BOTON ELIMINAR
                Button(
                    onClick = { navController.navigate(Routes.DELETE_EVENT) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar Eventos")
                }
                Spacer(modifier = Modifier.height(2.dp))

                // BOTON GESTION DE USUARIOS
                Button(
                    onClick = { navController.navigate(Routes.USERS_MANAGEMENT) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                ) {
                    Text("Gestión de Usuarios")
                }
                Spacer(modifier = Modifier.height(10.dp))

                // BUSCADOR
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    label = { Text("Buscar evento") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                // ESTADISTICAS
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = " Estadísticas", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Eventos: $totalEvents")
                        Text(text = "Inscritos: $totalAttendees")
                        Text(text = "Destacados: $featuredEvents")
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))

                // TÍTULO DE EVENTOS ACTIVOS
                Text(
                    text = "Eventos Activos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdeOscuro
                )
                Spacer(modifier = Modifier.height(2.dp))
            }

            // =========================================================
            // ELEMENTOS EN LISTA: EVENTOS FILTRADOS
            // =========================================================
            items(filteredEvents) { event ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (event.isFeatured) {
                            Text(text = " Evento Destacado", color = VerdeOscuro, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        Text(text = event.title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = VerdeOscuro)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(event.description)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(" ${event.location}")
                        Text(" ${event.date}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "${event.attendees}/${event.maxCapacity}", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = " ${viewModel.eventOccupation(event)}% ocupado")
                        Spacer(modifier = Modifier.height(12.dp))

                        // Fila de botones de control del evento
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = { viewModel.toggleFeatured(event.id) }) { Text("⭐") }
                            Button(onClick = { viewModel.toggleEventStatus(event.id) }) {
                                Text(if (event.isOpen) "Cerrar" else "Abrir")
                            }
                            Button(
                                onClick = { navController.navigate("${Routes.EDIT_EVENT}/${event.id}") },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("✏ Editar")
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // 📋 BOTÓN DE ASISTENCIA GLOBAL (CORREGIDO)
                        Button(
                            onClick = { navController.navigate("check_in_screen") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = VerdeOscuro)
                        ) {
                            Text("📋 Control de Asistencia")
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (event.isOpen) " Inscripciones abiertas" else " Evento cerrado",
                            color = if (event.isOpen) VerdeOscuro else MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            // =========================================================
            // ELEMENTOS EN LISTA: TÍTULO DE SUGERENCIAS
            // =========================================================
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Sugerencias de estudiantes",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdeOscuro
                )
                Spacer(modifier = Modifier.height(2.dp))
            }

            // =========================================================
            // ELEMENTOS EN LISTA: SUGERENCIAS
            // =========================================================
            items(suggestionViewModel.suggestions) { suggestion ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = suggestion.title, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(suggestion.description)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(" ${suggestion.location}")
                        Text(" ${suggestion.date}")
                        Spacer(modifier = Modifier.height(12.dp))

                        Row {
                            Button(
                                onClick = {
                                    suggestionViewModel.approveSuggestion(
                                        suggestionId = suggestion.id,
                                        onSuccess = { viewModel.refreshEvents() }
                                    )
                                }
                            ) {
                                Text("Aprobar")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { suggestionViewModel.rejectSuggestion(suggestionId = suggestion.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text("Rechazar")
                            }
                        }
                    }
                }
            }

            // Espaciado final inferior para que no quede pegado al borde físico
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}