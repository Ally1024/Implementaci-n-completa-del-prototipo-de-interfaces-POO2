package com.example.avancesproyecto.ui.theme.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.avancesproyecto.ui.theme.VerdeOscuro
import com.example.avancesproyecto.viewmodel.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEventScreen(
    eventId: Int,
    navController: NavHostController,
    viewModel: EventViewModel
) {

    // Extraccion del evento existente por su ID para precargar el formulario de modificacion
    val event = viewModel.events.find { it.id == eventId }

    if (event == null) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Evento no encontrado")
        }
        return
    }

    // Estados mutables inicializados con los datos historicos del registro seleccionado
    var nombre by remember { mutableStateOf(event.title) }
    var descripcion by remember { mutableStateOf(event.description) }
    var fecha by remember { mutableStateOf(event.date) }
    var locacion by remember { mutableStateOf(event.location) }
    var capacidad by remember { mutableStateOf(event.maxCapacity.toString()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Evento") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VerdeOscuro
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ==========================================
            // CAMPO: NOMBRE DEL EVENTO
            // ==========================================
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            // ==========================================
            // CAMPO: DESCRIPCION
            // ==========================================
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            // ==========================================
            // CAMPO: FECHA
            // ==========================================
            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it },
                label = { Text("Fecha") },
                modifier = Modifier.fillMaxWidth()
            )

            // ==========================================
            // CAMPO: LOCACION
            // ==========================================
            OutlinedTextField(
                value = locacion,
                onValueChange = { locacion = it },
                label = { Text("Locación") },
                modifier = Modifier.fillMaxWidth()
            )

            // ==========================================
            // CAMPO: CAPACIDAD LIMITE
            // ==========================================
            OutlinedTextField(
                value = capacidad,
                onValueChange = { capacidad = it },
                label = { Text("Capacidad") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ==========================================
            // BOTON: CONFIRMAR ACTUALIZACION
            // ==========================================
            Button(
                onClick = {
                    // ARREGLADO: Enlace de parametros en ingles respetando la arquitectura de EventViewModel
                    viewModel.editEvent(
                        eventId = event.id,
                        title = nombre,
                        description = descripcion,
                        date = fecha,
                        location = locacion,
                        maxCapacity = capacidad.toIntOrNull() ?: 0
                    )

                    // Retorno a la pila anterior del NavHost una vez guardados los cambios
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeOscuro
                )
            ) {
                Text(
                    text = "Guardar Cambios",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}