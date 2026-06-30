package com.example.avancesproyecto.ui.theme.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.avancesproyecto.model.Event
import com.example.avancesproyecto.viewmodel.EventViewModel

@Composable
fun EventCard(
    event: Event,
    viewModel: EventViewModel,
    onJoinClick: () -> Unit = {}
) {

    // Validacion logica para determinar si la capacidad maxima del evento fue alcanzada
    val isFull = event.attendees >= event.maxCapacity

    // Calculo matematico simple para obtener el remanente de cupos disponibles
    val remaining = event.maxCapacity - event.attendees

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            // TITULO DEL EVENTO
            Text(
                text = event.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            // DESCRIPCION GENERAL
            Text(text = event.description)

            Spacer(modifier = Modifier.height(6.dp))

            // METADATOS: FECHA Y UBICACION (Se eliminaron los emojis de calendario y pin)
            Text(text = "Fecha: ${event.date}")
            Text(text = "Lugar: ${event.location}")

            Spacer(modifier = Modifier.height(10.dp))

            // MANEJO DINAMICO DEL ESTADO DEL CUPO
            if (isFull) {
                Text(
                    text = "Evento lleno",
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = "Cupos disponibles: $remaining",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // BOTON DE CONTROL PARA INSCRIPCION
            Button(
                onClick = {
                    // Delegamos la accion directamente al callback estructurado.
                    // Esto permite redirigir al formulario de inscripcion de forma limpia.
                    onJoinClick()
                },
                enabled = !isFull, // Deshabilitacion automatica si el evento no cuenta con aforo
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
            ) {
                Text(
                    text = if (isFull) "Evento completo" else "Unirse al evento"
                )
            }
        }
    }
}