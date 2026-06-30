package com.example.avancesproyecto.ui.theme.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.avancesproyecto.ui.theme.VerdeOscuro
import com.example.avancesproyecto.viewmodel.EventViewModel

@Composable
fun InscripcionScreen(
    navController: NavHostController,
    viewModel: EventViewModel,
    eventId: Int
) {

    // Busqueda reactiva del evento objetivo dentro del listado global de la aplicacion
    val event = viewModel.events.find { it.id == eventId }

    if (event == null) {
        // Control de excepcion visual por si el parametro de navegacion falla o es alterado
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Evento no encontrado")
        }
        return
    }

    // Flag logico para validar el aforo remanente del evento antes de proceder con el registro
    val isFull = event.attendees >= event.maxCapacity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        // Titulo destacado del evento ambiental
        Text(
            text = event.title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = VerdeOscuro
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Bloque descriptivo del evento
        Text(text = event.description)

        Spacer(modifier = Modifier.height(10.dp))

        // Datos del evento limpios de emojis de calendario y mapa
        Text(text = "Fecha: ${event.date}")
        Text(text = "Lugar: ${event.location}")

        Spacer(modifier = Modifier.height(10.dp))

        // Indicador numerico de aforo
        Text(
            text = "Asistentes: ${event.attendees} / ${event.maxCapacity}",
            fontWeight = FontWeight.Bold
        )

        // Estado dinamico del cupo
        Text(
            text = if (isFull) "Evento lleno" else "Cupos disponibles",
            color = if (isFull) MaterialTheme.colorScheme.error else VerdeOscuro
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ==========================================
        // BOTON: CONFIRMACION DE OPERACION
        // ==========================================
        Button(
            onClick = {
                // 1. Registra la participacion del estudiante en el repositorio de datos
                viewModel.joinEvent(event.id)

                // 2. Sincroniza la lista en memoria para refrescar las pantallas previas
                viewModel.refreshEvents()

                // 3. Remueve la pantalla actual del stack de navegacion regresando de forma segura
                navController.popBackStack()
            },
            enabled = !isFull, // Proteccion nativa para impedir registros cuando el evento esta completo
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFull) MaterialTheme.colorScheme.error else VerdeOscuro
            )
        ) {
            Text("Confirmar inscripción")
        }
    }
}