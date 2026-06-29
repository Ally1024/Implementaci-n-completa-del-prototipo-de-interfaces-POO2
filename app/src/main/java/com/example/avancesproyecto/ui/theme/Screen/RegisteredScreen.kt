package com.example.avancesproyecto.ui.theme.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.avancesproyecto.ui.theme.VerdeOscuro
import com.example.avancesproyecto.viewmodel.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisteredScreen(
    viewModel: EventViewModel,
    navController: NavHostController
) {

    //  SOLO eventos con asistentes (simula "inscritos")
    val myEvents = viewModel.events.filter {
        it.attendees > 0
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Eventos Inscritos") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VerdeOscuro,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->

        if (myEvents.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No tienes eventos inscritos aún",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(myEvents) { event ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {

                        Column(modifier = Modifier.padding(16.dp)) {

                            Text(
                                text = event.title,
                                style = MaterialTheme.typography.headlineSmall,
                                color = VerdeOscuro
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(text = event.description)

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(" ${event.date}")
                                Text(" ${event.location}")
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = " ${event.attendees}/${event.maxCapacity}",
                                color = VerdeOscuro
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    //  ya no existe unregister, así que usamos joinEvent inverso simple
                                    // o puedes eliminar lógica si quieres mantener simple
                                },
                                modifier = Modifier.align(Alignment.End),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Text("Cancelar (no activo aún)")
                            }
                        }
                    }
                }
            }
        }
    }
}