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
fun AdminDashboardScreen(
    navController:  NavHostController,
    eventViewModel: EventViewModel,
    authViewModel:  AuthViewModel
) {
    val registrations by eventViewModel.registrations
    val attendance    by eventViewModel.attendance
    val events        = eventViewModel.events
    val users         = authViewModel.users

    val totalInscritos  = registrations.values.sumOf { it.size }
    val totalAsistieron = attendance.values.sumOf { m -> m.values.count { it } }

    Scaffold(
        topBar = {
            TopAppBar(
                title          = { Text("Base de Datos / Estadísticas") },
                navigationIcon = {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("← Atrás", color = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = VerdeOscuro, titleContentColor = White)
            )
        }
    ) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Resumen general
            item {
                Text("Resumen General", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = VerdeOscuro)
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard("📅 Eventos",    "${events.size}",        Modifier.weight(1f))
                    StatCard("👥 Alumnos",    "${users.size}",         Modifier.weight(1f))
                    StatCard("📝 Inscritos",  "$totalInscritos",       Modifier.weight(1f))
                    StatCard("✅ Asistieron", "$totalAsistieron",      Modifier.weight(1f))
                }
            }

            // Por evento
            item {
                Text("Por Evento", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = VerdeOscuro)
            }
            items(events) { event ->
                val inscritos  = registrations[event.id]?.size ?: 0
                val asistieron = attendance[event.id]?.values?.count { it } ?: 0
                val ausentes   = attendance[event.id]?.values?.count { !it } ?: 0

                Card(Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp),
                    shape = RoundedCornerShape(12.dp)) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(event.title, fontWeight = FontWeight.Bold, color = VerdeOscuro)
                        Text("📅 ${event.date}  🕐 ${event.time}", fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.outline)
                        HorizontalDivider(Modifier.padding(vertical = 4.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                            AttendStat("Inscritos",  inscritos.toString(),  Color(0xFF1565C0))
                            AttendStat("Asistieron", asistieron.toString(), Color(0xFF2E7D32))
                            AttendStat("Ausentes",   ausentes.toString(),   Color(0xFFC62828))
                        }
                    }
                }
            }

            // Por alumno
            item {
                Spacer(Modifier.height(4.dp))
                Text("Por Alumno", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = VerdeOscuro)
            }
            items(users) { user ->
                val inscritos   = registrations.values.count { it.contains(user.id) }
                val asistencias = eventViewModel.getTotalAttendedCount(user.id)

                Card(Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
                    Row(Modifier.fillMaxWidth().padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(user.name, fontWeight = FontWeight.Bold, color = VerdeOscuro)
                            Text("CIF: ${user.cif} | ${user.career}", fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.outline)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("📝 $inscritos eventos", fontSize = 13.sp)
                            Text("✅ $asistencias asistencias",
                                fontSize = 13.sp, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier, colors = CardDefaults.cardColors(containerColor = VerdeOscuro),
        shape = RoundedCornerShape(10.dp)) {
        Column(Modifier.padding(10.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, color = White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(label, color = White, fontSize = 11.sp)
        }
    }
}

@Composable
private fun AttendStat(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = color)
        Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
    }
}
