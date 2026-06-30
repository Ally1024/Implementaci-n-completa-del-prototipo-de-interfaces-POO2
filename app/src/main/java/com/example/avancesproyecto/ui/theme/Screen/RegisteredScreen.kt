package com.example.avancesproyecto.ui.theme.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.avancesproyecto.ui.theme.VerdeOscuro
import com.example.avancesproyecto.viewmodel.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisteredScreen(
    viewModel: EventViewModel,
    navController: NavHostController
) {

    // Filtrado local preventivo para renderizar unicamente eventos que registran participacion activa
    val myEvents = viewModel.events.filter { event ->
        event.attendees > 0
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Eventos Inscritos") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VerdeOscuro,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->

        // Evaluacion del estado de la coleccion para el renderizado condicional de la UI
        if (myEvents.isEmpty()) {
            // Estado vacio (Empty State) estructurado de forma limpia sin caracteres especiales
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No tienes eventos inscritos aún",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

        } else {
            // Renderizado optimizado mediante LazyColumn para listas con scroll eficiente
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(myEvents, key = { it.id }) { event ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {

                        Column(modifier = Modifier.padding(16.dp)) {

                            Text(
                                text = event.title,
                                style = MaterialTheme.typography.headlineSmall,
                                color = VerdeOscuro
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(text = event.description)

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Fecha: ${event.date}")
                                Text("Lugar: ${event.location}")
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Asistentes: ${event.attendees} / ${event.maxCapacity}",
                                color = VerdeOscuro
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // ==========================================
                            // CONTROL DE CANCELACION (PROCESO EN DESARROLLO)
                            // ==========================================
                            Button(
                                onClick = {
                                    // Espacio reservado para logica de desinscripcion local/remota en futuras iteraciones
                                },
                                modifier = Modifier.align(Alignment.End),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Text("Cancelar (no activo aún)")
                            }
                        }
                    }
                }
            }
        }
    }
}