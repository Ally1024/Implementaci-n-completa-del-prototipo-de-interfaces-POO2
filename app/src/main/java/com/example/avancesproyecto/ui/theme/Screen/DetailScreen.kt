package com.example.avancesproyecto.ui.theme.Screen

import android.content.Intent
import android.net.Uri
import android.widget.Toast
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

    // Búsqueda del evento en la lista del ViewModel según el ID recibido por parámetro
    val event = viewModel.events.find { it.id == id }

    if (event == null) {
        // Interfaz de error si por alguna razón el evento no existe en el repositorio local
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
                    Text("Fecha: ${event.date}")
                    Text("Lugar: ${event.location}")
                }

                // Información de aforo limpia de glifos
                Text(
                    text = "Asistentes: ${event.attendees} / ${event.maxCapacity}",
                    color = VerdeOscuro
                )

                Text(
                    text = if (isFull) "Evento lleno" else "Cupos disponibles",
                    color = if (isFull) MaterialTheme.colorScheme.error else VerdeOscuro
                )

                // ==========================================
                // BOTÓN: INTEGRACIÓN CON GOOGLE MAPS
                // ==========================================
                Button(
                    onClick = {
                        runCatching {
                            // CORREGIDO: Sintaxis de Intent de geolocalización por query de texto
                            val mapUri = Uri.parse("geo:0,0?q=${Uri.encode(event.location)}")
                            val intent = Intent(Intent.ACTION_VIEW, mapUri).apply {
                                // Forzamos a que intente abrir la app nativa de mapas si está disponible
                                setPackage("com.google.android.apps.maps")
                            }
                            context.startActivity(intent)
                        }.onFailure {
                            // Si el dispositivo no tiene Google Maps instalado, lanzamos un intent genérico de mapas web
                            val webMapUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(event.location)}")
                            val webIntent = Intent(Intent.ACTION_VIEW, webMapUri)
                            context.startActivity(webIntent)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdeOscuro
                    )
                ) {
                    Text("Abrir en Google Maps")
                }

                // ==========================================
                // BOTÓN: CONTROL DE INSCRIPCIÓN
                // ==========================================
                Button(
                    onClick = {
                        if (!isFull) {
                            viewModel.joinEvent(event.id)
                            Toast.makeText(context, "Inscripción procesada con éxito", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = !isFull,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isFull) MaterialTheme.colorScheme.error else VerdeOscuro
                    )
                ) {
                    Text(
                        text = if (isFull) "Completo" else "Inscribirse"
                    )
                }
            }
        }
    }
}