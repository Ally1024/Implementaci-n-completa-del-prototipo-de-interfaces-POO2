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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.avancesproyecto.ui.theme.VerdeOscuro
import com.example.avancesproyecto.ui.theme.White
import com.example.avancesproyecto.viewmodel.AuthViewModel
import com.example.avancesproyecto.viewmodel.EventViewModel
import com.example.avancesproyecto.viewmodel.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlumnoPerfilScreen(
    navController:  NavHostController,
    authViewModel:  AuthViewModel,
    eventViewModel: EventViewModel
) {
    val user           = SessionManager.currentUser ?: return
    val attendance     by eventViewModel.attendance
    val registrations  by eventViewModel.registrations
    val asistidos      = eventViewModel.getUserAttendedEvents(user.id)
    val totalInscritos = registrations.values.count { it.contains(user.id) }
    val totalAsistidos = eventViewModel.getTotalAttendedCount(user.id)

    var oldPass    by remember { mutableStateOf("") }
    var newPass    by remember { mutableStateOf("") }
    var newPass2   by remember { mutableStateOf("") }
    var passMsg    by remember { mutableStateOf("") }
    var passMsgOk  by remember { mutableStateOf(false) }
    var showPass   by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title          = { Text("Mi Perfil") },
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
            // Datos del alumno
            item {
                Card(Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp),
                    shape = RoundedCornerShape(14.dp)) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("👤 Mis Datos", fontWeight = FontWeight.Bold, color = VerdeOscuro, fontSize = 17.sp)
                        HorizontalDivider()
                        ProfileRow("Nombre",   user.name)
                        ProfileRow("CIF",      user.cif)
                        ProfileRow("Carrera",  user.career)
                        ProfileRow("📝 Inscritos",  "$totalInscritos eventos")
                        ProfileRow("✅ Asistidos",  "$totalAsistidos eventos")
                    }
                }
            }

            // Cambiar contraseña
            item {
                Card(Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp),
                    shape = RoundedCornerShape(14.dp)) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("🔑 Cambiar Contraseña", fontWeight = FontWeight.Bold,
                            color = VerdeOscuro, fontSize = 17.sp)
                        HorizontalDivider()

                        val vis = if (showPass) androidx.compose.ui.text.input.VisualTransformation.None
                                  else PasswordVisualTransformation()

                        OutlinedTextField(oldPass, { oldPass = it; passMsg = "" },
                            label = { Text("Contraseña actual") }, modifier = Modifier.fillMaxWidth(),
                            visualTransformation = vis)
                        OutlinedTextField(newPass, { newPass = it; passMsg = "" },
                            label = { Text("Nueva contraseña") }, modifier = Modifier.fillMaxWidth(),
                            visualTransformation = vis)
                        OutlinedTextField(newPass2, { newPass2 = it; passMsg = "" },
                            label = { Text("Confirmar nueva contraseña") }, modifier = Modifier.fillMaxWidth(),
                            visualTransformation = vis)

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(showPass, { showPass = it })
                            Text("Mostrar contraseñas", fontSize = 13.sp)
                        }

                        if (passMsg.isNotEmpty())
                            Text(passMsg,
                                color    = if (passMsgOk) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error,
                                fontSize = 13.sp, fontWeight = FontWeight.Bold)

                        Button(
                            onClick = {
                                when {
                                    oldPass.isBlank() || newPass.isBlank() || newPass2.isBlank() -> {
                                        passMsg = "Completa todos los campos."; passMsgOk = false
                                    }
                                    oldPass != user.password -> {
                                        passMsg = "La contraseña actual es incorrecta."; passMsgOk = false
                                    }
                                    newPass.length < 4 -> {
                                        passMsg = "La nueva contraseña debe tener al menos 4 caracteres."; passMsgOk = false
                                    }
                                    newPass != newPass2 -> {
                                        passMsg = "Las contraseñas nuevas no coinciden."; passMsgOk = false
                                    }
                                    else -> {
                                        authViewModel.changePassword(user.id, newPass)
                                        oldPass = ""; newPass = ""; newPass2 = ""
                                        passMsg = "✅ Contraseña cambiada exitosamente."; passMsgOk = true
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape    = RoundedCornerShape(50.dp),
                            colors   = ButtonDefaults.buttonColors(containerColor = VerdeOscuro)
                        ) { Text("Actualizar contraseña", fontWeight = FontWeight.Bold) }
                    }
                }
            }

            // Eventos asistidos (solo los validados por admin)
            item {
                Text("✅ Mis Eventos Asistidos", fontWeight = FontWeight.Bold,
                    color = VerdeOscuro, fontSize = 17.sp)
                Text("Solo aparecen los validados por el administrador.",
                    fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
            }

            if (asistidos.isEmpty()) {
                item {
                    Card(Modifier.fillMaxWidth()) {
                        Box(Modifier.padding(20.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text("Aún no tienes eventos validados por el administrador.",
                                color = MaterialTheme.colorScheme.outline)
                        }
                    }
                }
            } else {
                items(asistidos) { event ->
                    Card(Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
                        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(event.title, fontWeight = FontWeight.Bold, color = VerdeOscuro)
                            Text("📅 ${event.date}  🕐 ${event.time}", fontSize = 12.sp)
                            Text("📍 ${event.location}", fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.outline)
                            Text("✅ Asistencia confirmada", color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label,  fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.outline, fontSize = 14.sp)
        Text(value, fontWeight = FontWeight.Bold,   color = VerdeOscuro,                       fontSize = 14.sp)
    }
}
