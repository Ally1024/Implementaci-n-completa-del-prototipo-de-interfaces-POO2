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

    val isFull = event.attendees >= event.maxCapacity
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

            //  TÍTULO
            Text(
                text = event.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            // DESCRIPCIÓN
            Text(text = event.description)

            Spacer(modifier = Modifier.height(6.dp))

            // FECHA Y LOCACIÓN
            Text(text = "📅 ${event.date}")
            Text(text = "📍 ${event.location}")

            Spacer(modifier = Modifier.height(10.dp))

            //  ESTADO DEL EVENTO
            if (isFull) {
                Text(
                    text = " Evento lleno",
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = " Cupos disponibles: $remaining",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            //  BOTÓN DE INSCRIPCIÓN
            Button(
                onClick = {
                    viewModel.joinEvent(event.id)
                    onJoinClick()
                },
                enabled = !isFull,
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