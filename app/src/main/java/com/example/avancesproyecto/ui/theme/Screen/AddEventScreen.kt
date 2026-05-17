package com.example.avancesproyecto.ui.theme.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.avancesproyecto.navigation.Routes
import com.example.avancesproyecto.ui.theme.GrisClaro
import com.example.avancesproyecto.ui.theme.VerdeOscuro
import com.example.avancesproyecto.ui.theme.White
import com.example.avancesproyecto.viewmodel.EventViewModel

@Composable
fun AddEventScreen(
    navController: NavHostController,
    viewModel: EventViewModel
) {

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var locacion by remember { mutableStateOf("") }

    // 🔥 NUEVO: capacidad máxima
    var capacidad by remember { mutableStateOf("") }

    var errorMensaje by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(VerdeOscuro, White)
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Agregar Evento",
                fontSize = 30.sp,
                color = White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(28.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = White)
            ) {

                Column(modifier = Modifier.padding(22.dp)) {

                    // NOMBRE
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = {
                            nombre = it
                            errorMensaje = ""
                        },
                        label = { Text("Nombre del evento") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // DESCRIPCIÓN
                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = {
                            descripcion = it
                            errorMensaje = ""
                        },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // FECHA
                    OutlinedTextField(
                        value = fecha,
                        onValueChange = {
                            fecha = it
                            errorMensaje = ""
                        },
                        label = { Text("Fecha") },
                        placeholder = { Text("2026-05-30") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // LOCACIÓN
                    OutlinedTextField(
                        value = locacion,
                        onValueChange = {
                            locacion = it
                            errorMensaje = ""
                        },
                        label = { Text("Locación") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 🔥 CAPACIDAD MÁXIMA (NUEVO)
                    OutlinedTextField(
                        value = capacidad,
                        onValueChange = {
                            capacidad = it
                            errorMensaje = ""
                        },
                        label = { Text("Capacidad máxima") },
                        placeholder = { Text("Ej: 50") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // ERROR
                    if (errorMensaje.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = errorMensaje,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // BOTÓN GUARDAR
                    Button(
                        onClick = {

                            val cap = capacidad.toIntOrNull()

                            when {

                                nombre.isBlank() ->
                                    errorMensaje = "Ingrese el nombre del evento"

                                descripcion.isBlank() ->
                                    errorMensaje = "Ingrese una descripción"

                                fecha.isBlank() ->
                                    errorMensaje = "Ingrese una fecha"

                                locacion.isBlank() ->
                                    errorMensaje = "Ingrese una locación"

                                cap == null || cap <= 0 ->
                                    errorMensaje = "Ingrese una capacidad válida"

                                else -> {

                                    viewModel.addEvent(
                                        nombre,
                                        descripcion,
                                        fecha,
                                        locacion,
                                        cap // 🔥 NUEVO
                                    )

                                    navController.navigate(Routes.ADMIN) {
                                        popUpTo(Routes.ADD_EVENT) {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VerdeOscuro,
                            contentColor = White
                        )
                    ) {
                        Text(
                            text = "Guardar Evento",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}