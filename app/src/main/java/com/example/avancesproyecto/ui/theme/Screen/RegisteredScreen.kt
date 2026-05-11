package com.example.avancesproyecto.ui.theme.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

    val registeredEvents by
    viewModel.registeredEvents

    val myEvents =
        viewModel.events.value.filter {

            registeredEvents.contains(it.id)
        }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        text = "Mis Eventos Inscritos"
                    )
                },

                colors =
                    TopAppBarDefaults.topAppBarColors(

                        containerColor =
                            VerdeOscuro,

                        titleContentColor =
                            MaterialTheme.colorScheme.onPrimary
                    ),

                navigationIcon = {

                    IconButton(

                        onClick = {
                            navController.navigateUp()
                        }
                    ) {

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

                contentAlignment =
                    Alignment.Center
            ) {

                Text(

                    text =
                        "No tienes eventos inscritos aún. ¡Inscríbete en la pantalla principal!",

                    style =
                        MaterialTheme.typography.bodyLarge,

                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

        } else {

            LazyColumn(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),

                verticalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                items(myEvents) { event ->

                    Card(

                        modifier = Modifier.fillMaxWidth(),

                        elevation =
                            CardDefaults.cardElevation(4.dp)
                    ) {

                        Column(

                            modifier =
                                Modifier.padding(16.dp)
                        ) {

                            Text(

                                text = event.title,

                                style =
                                    MaterialTheme.typography.headlineSmall,

                                color = VerdeOscuro
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(8.dp)
                            )

                            Text(

                                text = event.description,

                                style =
                                    MaterialTheme.typography.bodyMedium
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(8.dp)
                            )

                            Row(

                                modifier =
                                    Modifier.fillMaxWidth(),

                                horizontalArrangement =
                                    Arrangement.SpaceBetween
                            ) {

                                Text(

                                    text =
                                        "📅 ${event.date}",

                                    style =
                                        MaterialTheme.typography.bodySmall
                                )

                                Text(

                                    text =
                                        "📍 ${event.location}",

                                    style =
                                        MaterialTheme.typography.bodySmall
                                )
                            }

                            Spacer(
                                modifier =
                                    Modifier.height(10.dp)
                            )

                            Text(

                                text =
                                    "👥 Cupo máximo: 50 estudiantes",

                                color = VerdeOscuro
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(12.dp)
                            )

                            Button(

                                onClick = {

                                    viewModel.unregisterFromEvent(
                                        event.id
                                    )
                                },

                                modifier =
                                    Modifier.align(
                                        Alignment.End
                                    ),

                                colors =
                                    ButtonDefaults.buttonColors(

                                        containerColor =
                                            MaterialTheme.colorScheme.error
                                    )
                            ) {

                                Text(
                                    text =
                                        "Cancelar Inscripción"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}