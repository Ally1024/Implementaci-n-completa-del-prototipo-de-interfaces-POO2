package com.example.avancesproyecto.ui.theme.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.avancesproyecto.ui.theme.VerdeOscuro
import com.example.avancesproyecto.ui.theme.White
import com.example.avancesproyecto.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsuariosScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    var showDialog  by remember { mutableStateOf(false) }
    var newNombre   by remember { mutableStateOf("") }
    var newCif      by remember { mutableStateOf("") }
    var newCarrera  by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var dialogError by remember { mutableStateOf("") }
    var showPass    by remember { mutableStateOf(false) }

    val users = authViewModel.users

    // ── Diálogo agregar alumno ─────────────────────────
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false; dialogError = "" },
            title = { Text("Nuevo Alumno", color = VerdeOscuro, fontWeight = FontWeight.Bold) },
            text  = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                    OutlinedTextField(
                        value         = newNombre,
                        onValueChange = { newNombre = it; dialogError = "" },
                        label         = { Text("Nombre completo") },
                        modifier      = Modifier.fillMaxWidth()
                    )

                    // CIF alumno: solo dígitos, máximo 8
                    OutlinedTextField(
                        value         = newCif,
                        onValueChange = { input ->
                            newCif = input.filter { it.isDigit() }.take(8)
                            dialogError = ""
                        },
                        label          = { Text("CIF (8 dígitos)") },
                        supportingText = {
                            Text("${newCif.length}/8 dígitos",
                                color = if (newCif.length == 8) VerdeOscuro
                                        else MaterialTheme.colorScheme.outline)
                        },
                        isError         = newCif.isNotEmpty() && newCif.length != 8,
                        singleLine      = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier        = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value         = newCarrera,
                        onValueChange = { newCarrera = it; dialogError = "" },
                        label         = { Text("Carrera") },
                        modifier      = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value                = newPassword,
                        onValueChange        = { newPassword = it; dialogError = "" },
                        label                = { Text("Contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier             = Modifier.fillMaxWidth()
                    )

                    if (dialogError.isNotEmpty())
                        Text(dialogError, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        when {
                            newNombre.isBlank()   -> dialogError = "El nombre es obligatorio."
                            newCif.isBlank()      -> dialogError = "El CIF es obligatorio."
                            newCif.length != 8    -> dialogError = "El CIF debe tener exactamente 8 dígitos."
                            !newCif.all { it.isDigit() } -> dialogError = "El CIF solo puede contener números."
                            newCarrera.isBlank()  -> dialogError = "La carrera es obligatoria."
                            newPassword.isBlank() -> dialogError = "La contraseña es obligatoria."
                            authViewModel.users.any { it.cif == newCif } ->
                                dialogError = "Ya existe un alumno con ese CIF."
                            else -> {
                                authViewModel.addUser(newNombre, newCif, newCarrera, newPassword)
                                newNombre = ""; newCif = ""; newCarrera = ""
                                newPassword = ""; dialogError = ""; showDialog = false
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeOscuro)
                ) { Text("Agregar") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false; dialogError = "" }) { Text("Cancelar") }
            }
        )
    }

    // ── Pantalla principal ─────────────────────────────
    Scaffold(
        topBar = {
            TopAppBar(
                title          = { Text("Gestionar Usuarios (${users.size})") },
                navigationIcon = {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("← Atrás", color = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VerdeOscuro, titleContentColor = White
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            Row(
                Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { showDialog = true },
                    colors  = ButtonDefaults.buttonColors(containerColor = VerdeOscuro)
                ) { Text("➕ Agregar alumno") }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Ver contraseñas", fontSize = 12.sp)
                    Switch(
                        checked         = showPass,
                        onCheckedChange = { showPass = it },
                        colors          = SwitchDefaults.colors(
                            checkedThumbColor = VerdeOscuro,
                            checkedTrackColor = VerdeOscuro.copy(alpha = 0.4f)
                        )
                    )
                }
            }

            if (users.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay alumnos registrados.")
                }
            } else {
                LazyColumn(
                    Modifier.fillMaxSize().padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(users, key = { it.id }) { user ->
                        Card(Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
                            Row(
                                Modifier.fillMaxWidth().padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment     = Alignment.CenterVertically
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text(user.name, fontWeight = FontWeight.Bold, color = VerdeOscuro)
                                    Text("CIF: ${user.cif}", fontSize = 13.sp)
                                    Text("Carrera: ${user.career}", fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.outline)
                                    if (showPass)
                                        Text("🔑 ${user.password}", fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.error)
                                }
                                TextButton(
                                    onClick = { authViewModel.deleteUser(user.id) },
                                    colors  = ButtonDefaults.textButtonColors(
                                        contentColor = MaterialTheme.colorScheme.error
                                    )
                                ) { Text("🗑️ Eliminar") }
                            }
                        }
                    }
                }
            }
        }
    }
}
