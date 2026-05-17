package com.example.avancesproyecto.ui.theme.Screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.avancesproyecto.ui.theme.VerdeOscuro
import com.example.avancesproyecto.viewmodel.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    id: Int,
    viewModel: EventViewModel,
    navController: NavHostController
) {

    val event = viewModel.events.find { it.id == id }

    if (event == null) {

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
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver"
                            )
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

        val context = LocalContext.current
        val isFull = event.attendees >= event.maxCapacity

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
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver"
                            )
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

                Text(text = event.description)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("📅 ${event.date}")
                    Text("📍 ${event.location}")
                }

                Text(
                    text = "👥 ${event.attendees}/${event.maxCapacity}",
                    color = VerdeOscuro
                )

                Text(
                    text = if (isFull) "🔴 Evento lleno"
                    else "🟢 Cupos disponibles",
                    color = if (isFull)
                        MaterialTheme.colorScheme.error
                    else
                        VerdeOscuro
                )

                // 🌍 BOTÓN GOOGLE MAPS
                Button(
                    onClick = {
                        val uri = Uri.parse(
                            "https://www.google.com/maps/search/?api=1&query=${event.location}"
                        )

                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdeOscuro
                    )
                ) {
                    Text("Abrir en Google Maps")
                }

                // 🟢 BOTÓN INSCRIBIRSE
                Button(
                    onClick = {
                        if (!isFull) {
                            viewModel.joinEvent(event.id)
                        }
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
                    Text(
                        if (isFull) "Completo"
                        else "Inscribirse"
                    )
                }
            }
        }
    }
}