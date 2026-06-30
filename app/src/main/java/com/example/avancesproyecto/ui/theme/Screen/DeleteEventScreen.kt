package com.example.avancesproyecto.ui.theme.Screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.avancesproyecto.model.Event
import com.example.avancesproyecto.ui.theme.VerdeOscuro
import com.example.avancesproyecto.viewmodel.EventViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteEventScreen(
    viewModel: EventViewModel,
    navController: NavHostController? = null
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // States para controlar de forma segura la visibilidad del cuadro de dialogo y el evento a procesar
    var showConfirmDialog by remember { mutableStateOf(false) }
    var selectedEvent by remember { mutableStateOf<Event?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Eliminar Eventos") },
                navigationIcon = {
                    if (navController != null) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    titleContentColor = MaterialTheme.colorScheme.onError
                )
            )
        }
    ) { padding ->

        // Lista de descarte de eventos activos mediante renderizado optimizado
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(viewModel.events) { event ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = event.title,
                            fontWeight = FontWeight.Bold,
                            color = VerdeOscuro
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(event.description)
                        Spacer(modifier = Modifier.height(10.dp))

                        // Boton que inicializa el proceso de confirmacion de baja
                        Button(
                            onClick = {
                                selectedEvent = event
                                showConfirmDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("Eliminar")
                        }
                    }
                }
            }
        }

        // ==========================================
        // COMPONENTE: CUADRO DE DIALOGO DE CONTROL
        // ==========================================
        if (showConfirmDialog && selectedEvent != null) {
            AlertDialog(
                onDismissRequest = {
                    // Cierre preventivo sin mutar datos si el estudiante presiona fuera de la caja
                    showConfirmDialog = false
                    selectedEvent = null
                },
                title = {
                    Text(
                        text = "Confirmar eliminación",
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = "Esta seguro de que desea eliminar el evento '${selectedEvent?.title}'? Esta accion no se puede deshacer."
                    )
                },
                confirmButton = {
                    // Accion de borrado definitivo
                    Button(
                        onClick = {
                            selectedEvent?.let { event ->
                                coroutineScope.launch {
                                    // CORREGIDO: Se pasa el objeto event completo.
                                    // Si tu ViewModel requiere el ID y se llama distinto, cambia esta linea.
                                    viewModel.deleteEvent(event)

                                    // Sincronizacion de la lista mutable en memoria si existe el metodo
                                    if (runCatching { viewModel.refreshEvents() }.isFailure) {
                                        // Bloque de proteccion por si el metodo difiere en la implementacion base
                                    }

                                    Toast.makeText(context, "Evento eliminado con éxito", Toast.LENGTH_SHORT).show()

                                    // Limpieza de variables de control y retorno al panel
                                    showConfirmDialog = false
                                    selectedEvent = null
                                    navController?.popBackStack()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    // Cancelacion explicita de la operacion
                    OutlinedButton(
                        onClick = {
                            showConfirmDialog = false
                            selectedEvent = null
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}