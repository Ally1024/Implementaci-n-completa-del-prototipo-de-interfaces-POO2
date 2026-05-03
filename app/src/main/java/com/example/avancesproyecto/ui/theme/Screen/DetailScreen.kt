package com.example.avancesproyecto.ui.theme.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.avancesproyecto.viewmodel.EventViewModel
import com.example.avancesproyecto.ui.theme.VerdeOscuro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(id: Int, viewModel: EventViewModel, navController: NavHostController) {
    val event = viewModel.events.find { it.id == id }
    val registeredEvents by viewModel.registeredEvents
    val isRegistered = registeredEvents.contains(id)

    if (event == null) {
        // Handle case where event is not found
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Evento no encontrado") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = VerdeOscuro,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Evento no encontrado")
            }
        }
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(event.title) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = VerdeOscuro,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = VerdeOscuro
                )
                Text(
                    text = event.description,
                    style = MaterialTheme.typography.bodyLarge
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "📅 ${event.date}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "📍 ${event.location}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (isRegistered) {
                            viewModel.unregisterFromEvent(event.id)
                        } else {
                            viewModel.registerForEvent(event.id)
                        }
                    },
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isRegistered) MaterialTheme.colorScheme.error else VerdeOscuro
                    )
                ) {
                    Text(if (isRegistered) "Cancelar Inscripción" else "Inscribirse")
                }
            }
        }
    }
}
