package com.example.avancesproyecto.ui.theme.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.avancesproyecto.ui.theme.VerdeOscuro
import com.example.avancesproyecto.ui.theme.White
import com.example.avancesproyecto.viewmodel.AuthViewModel
import com.example.avancesproyecto.viewmodel.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAsistenciaScreen(
    navController:  NavHostController,
    eventViewModel: EventViewModel,
    authViewModel:  AuthViewModel
) {
    var selectedEventId by remember { mutableIntStateOf(-1) }
    val registrations   by eventViewModel.registrations
    val attendance      by eventViewModel.attendance
    val events          = eventViewModel.events

    Scaffold(
        topBar = {
            TopAppBar(
                title          = { Text("Control de Asistencia") },
                navigationIcon = {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("← Atrás", color = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = VerdeOscuro, titleContentColor = White)
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {

            // Selector de evento
            Text("Selecciona el evento:", fontWeight = FontWeight.Bold, color = VerdeOscuro)
            Spacer(Modifier.height(8.dp))

            events.forEach { event ->
                val isSelected = selectedEventId == event.id
                val inscritos  = registrations[event.id]?.size ?: 0
                FilterChip(
                    selected = isSelected,
                    onClick  = { selectedEventId = event.id },
                    label    = {
                        Text("${event.title} — ${event.date} ${event.time} ($inscritos inscritos)")
                    },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                )
            }

            if (selectedEventId < 0) {
                Spacer(Modifier.height(32.dp))
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Selecciona un evento para ver los inscritos.", color = MaterialTheme.colorScheme.outline)
                }
                return@Scaffold
            }

            val event            = eventViewModel.getEventById(selectedEventId)
            val registeredUsers  = registrations[selectedEventId] ?: emptySet()

            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(Modifier.height(8.dp))

            Text("Alumnos inscritos en: ${event?.title}",
                fontWeight = FontWeight.Bold, color = VerdeOscuro, fontSize = 15.sp)
            Text("Fecha: ${event?.date}  Hora: ${event?.time}", fontSize = 12.sp,
                color = MaterialTheme.colorScheme.outline)

            Spacer(Modifier.height(8.dp))

            if (registeredUsers.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nadie se ha inscrito aún a este evento.")
                }
            } else {
                LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(registeredUsers.toList()) { userId ->
                        val user       = authViewModel.getUserById(userId)
                        val asistencia = attendance[selectedEventId]?.get(userId) // null/true/false

                        Card(Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
                            Row(
                                Modifier.fillMaxWidth().padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment     = Alignment.CenterVertically
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text(user?.name ?: "Usuario $userId",
                                        fontWeight = FontWeight.Bold, color = VerdeOscuro)
                                    Text("CIF: ${user?.cif ?: "-"}", fontSize = 12.sp)
                                    Text("Carrera: ${user?.career ?: "-"}", fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.outline)
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    // Botón ASISTIÓ ✓
                                    Button(
                                        onClick = { eventViewModel.markAttendance(selectedEventId, userId, true) },
                                        colors  = ButtonDefaults.buttonColors(
                                            containerColor = if (asistencia == true)
                                                Color(0xFF2E7D32) else Color(0xFF81C784)
                                        ),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                        shape = RoundedCornerShape(8.dp)
                                    ) { Text("✓", fontSize = 18.sp, color = White) }

                                    // Botón NO ASISTIÓ ✗
                                    Button(
                                        onClick = { eventViewModel.markAttendance(selectedEventId, userId, false) },
                                        colors  = ButtonDefaults.buttonColors(
                                            containerColor = if (asistencia == false)
                                                Color(0xFFC62828) else Color(0xFFEF9A9A)
                                        ),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                        shape = RoundedCornerShape(8.dp)
                                    ) { Text("✗", fontSize = 18.sp, color = White) }
                                }
                            }

                            // Estado actual
                            val estadoText  = when (asistencia) {
                                true  -> "✅ Asistió"
                                false -> "❌ No asistió"
                                null  -> "⏳ Pendiente"
                            }
                            val estadoColor = when (asistencia) {
                                true  -> Color(0xFF2E7D32)
                                false -> Color(0xFFC62828)
                                null  -> MaterialTheme.colorScheme.outline
                            }
                            Text(estadoText, color = estadoColor, fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 12.dp, bottom = 8.dp))
                        }
                    }
                }
            }
        }
    }
}
