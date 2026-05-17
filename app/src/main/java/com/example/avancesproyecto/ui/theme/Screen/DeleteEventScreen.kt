package com.example.avancesproyecto.ui.theme.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.avancesproyecto.model.Event
import com.example.avancesproyecto.ui.theme.VerdeOscuro
import com.example.avancesproyecto.viewmodel.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteEventScreen(
    viewModel: EventViewModel
) {

    // =========================
    // ESTADO DEL DIALOGO
    // =========================

    val showConfirmDialog = remember { mutableStateOf(false) }
    val selectedEvent = remember { mutableStateOf<Event?>(null) }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text("Eliminar Eventos")
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            )
        }

    ) { padding ->

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

                        Button(

                            onClick = {
                                selectedEvent.value = event
                                showConfirmDialog.value = true
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

        // =========================
        // DIALOGO DE CONFIRMACION
        // =========================

        if (showConfirmDialog.value && selectedEvent.value != null) {

            AlertDialog(
                onDismissRequest = {
                    showConfirmDialog.value = false
                    selectedEvent.value = null
                },

                title = {
                    Text(
                        text = "Confirmar eliminación",
                        fontWeight = FontWeight.Bold
                    )
                },

                text = {
                    Text(
                        text = "¿Estás seguro de que deseas eliminar el evento '${selectedEvent.value?.title}'? Esta acción no se puede deshacer."
                    )
                },

                confirmButton = {
                    Button(
                        onClick = {
                            selectedEvent.value?.let {
                                viewModel.deleteEvent(it.id)
                            }
                            showConfirmDialog.value = false
                            selectedEvent.value = null
                        },

                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Eliminar")
                    }
                },

                dismissButton = {
                    OutlinedButton(
                        onClick = {
                            showConfirmDialog.value = false
                            selectedEvent.value = null
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}