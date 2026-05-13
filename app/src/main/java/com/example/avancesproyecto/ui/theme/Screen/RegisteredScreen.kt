package com.example.avancesproyecto.ui.theme.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.avancesproyecto.ui.theme.VerdeOscuro
import com.example.avancesproyecto.ui.theme.White
import com.example.avancesproyecto.viewmodel.EventViewModel
import com.example.avancesproyecto.viewmodel.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisteredScreen(viewModel: EventViewModel, navController: NavHostController) {
    val registrations by viewModel.registrations
    val userId        = SessionManager.currentUser?.id ?: -1

    // Eventos en los que el alumno actual está inscrito
    val myEvents = viewModel.events.filter { event ->
        registrations[event.id]?.contains(userId) == true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Eventos Inscritos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás", tint = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor    = VerdeOscuro,
                    titleContentColor = White
                )
            )
        }
    ) { padding ->
        if (myEvents.isEmpty()) {
            Box(
                modifier         = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text  = "No tienes eventos inscritos aún.\n¡Inscríbete en la pantalla principal!",
                    color = MaterialTheme.colorScheme.outline
                )
            }
        } else {
            LazyColumn(
                modifier            = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(myEvents) { event ->
                    Card(
                        modifier  = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(event.title,
                                style      = MaterialTheme.typography.titleMedium,
                                color      = VerdeOscuro,
                                fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                            Text(event.description,
                                style    = MaterialTheme.typography.bodySmall,
                                maxLines = 2)
                            Spacer(Modifier.height(8.dp))
                            Row(Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("📅 ${event.date}  🕐 ${event.time}",
                                    style = MaterialTheme.typography.bodySmall)
                                Text("📍 ${event.location.take(20)}…",
                                    style = MaterialTheme.typography.bodySmall)
                            }
                            Spacer(Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    if (userId >= 0)
                                        viewModel.unregisterFromEvent(event.id, userId)
                                },
                                modifier = Modifier.align(Alignment.End),
                                colors   = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                                )
                            ) { Text("Cancelar Inscripción") }
                        }
                    }
                }
            }
        }
    }
}
