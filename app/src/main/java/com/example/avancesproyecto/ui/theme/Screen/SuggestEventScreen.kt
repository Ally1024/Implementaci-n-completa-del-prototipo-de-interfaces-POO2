package com.example.avancesproyecto.ui.theme.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.avancesproyecto.ui.theme.GrisClaro
import com.example.avancesproyecto.ui.theme.VerdeOscuro
import com.example.avancesproyecto.viewmodel.SuggestionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestEventScreen(
    navController: NavHostController,
    suggestionViewModel: SuggestionViewModel
) {

    // Estados mutables para el control del formulario de sugerencias
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var locacion by remember { mutableStateOf("") }
    var capacidad by remember { mutableStateOf("") }

    // Estados para la gestion y despliegue de errores de validacion en la UI
    var showErrorMessage by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sugerir Evento") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VerdeOscuro,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ==========================================
            // CAMPO: NOMBRE DE LA SUGERENCIA
            // ==========================================
            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    showErrorMessage = false
                },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = VerdeOscuro),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = VerdeOscuro,
                    unfocusedTextColor = VerdeOscuro,
                    focusedBorderColor = VerdeOscuro,
                    unfocusedBorderColor = GrisClaro,
                    cursorColor = VerdeOscuro
                )
            )

            // ==========================================
            // CAMPO: DESCRIPCION DETALLADA
            // ==========================================
            OutlinedTextField(
                value = descripcion,
                onValueChange = {
                    descripcion = it
                    showErrorMessage = false
                },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = VerdeOscuro),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = VerdeOscuro,
                    unfocusedTextColor = VerdeOscuro,
                    focusedBorderColor = VerdeOscuro,
                    unfocusedBorderColor = GrisClaro,
                    cursorColor = VerdeOscuro
                )
            )

            // ==========================================
            // CAMPO: FECHA DEL EVENTO PROPUESTO
            // ==========================================
            OutlinedTextField(
                value = fecha,
                onValueChange = {
                    fecha = it
                    showErrorMessage = false
                },
                label = { Text("Fecha") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = VerdeOscuro),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = VerdeOscuro,
                    unfocusedTextColor = VerdeOscuro,
                    focusedBorderColor = VerdeOscuro,
                    unfocusedBorderColor = GrisClaro,
                    cursorColor = VerdeOscuro
                )
            )

            // ==========================================
            // CAMPO: LOCACION / ESPACIO SUGERIDO
            // ==========================================
            OutlinedTextField(
                value = locacion,
                onValueChange = {
                    locacion = it
                    showErrorMessage = false
                },
                label = { Text("Locación") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = VerdeOscuro),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = VerdeOscuro,
                    unfocusedTextColor = VerdeOscuro,
                    focusedBorderColor = VerdeOscuro,
                    unfocusedBorderColor = GrisClaro,
                    cursorColor = VerdeOscuro
                )
            )

            // ==========================================
            // CAMPO: ESTIMACION DE CAPACIDAD
            // ==========================================
            OutlinedTextField(
                value = capacidad,
                onValueChange = {
                    capacidad = it
                    showErrorMessage = false
                },
                label = { Text("Capacidad") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = VerdeOscuro),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = VerdeOscuro,
                    unfocusedTextColor = VerdeOscuro,
                    focusedBorderColor = VerdeOscuro,
                    unfocusedBorderColor = GrisClaro,
                    cursorColor = VerdeOscuro
                )
            )

            // Contenedor dinamico para alertas de error del formulario
            if (showErrorMessage) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // ==========================================
            // BOTON: EMISION DE PROPUESTA AL VIEWMODEL
            // ==========================================
            Button(
                onClick = {
                    // Validacion basica en cliente antes del envio
                    if (nombre.isBlank() || descripcion.isBlank() || fecha.isBlank() || locacion.isBlank()) {
                        showErrorMessage = true
                        errorMessage = "Completa todos los campos"
                        return@Button
                    }

                    // Despacho asincrono de la sugerencia mediante callbacks estructurados
                    suggestionViewModel.addSuggestion(
                        title = nombre,
                        description = descripcion,
                        date = fecha,
                        location = locacion,
                        capacity = capacidad.toIntOrNull() ?: 0,
                        onSuccess = {
                            // Retorno seguro en la pila de navegacion si es aceptada
                            navController.popBackStack()
                        },
                        onError = { error ->
                            // Captura el mensaje de error personalizado provisto por el backend/viewmodel
                            errorMessage = error
                            showErrorMessage = true
                        }
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeOscuro
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar sugerencia")
            }
        }
    }
}