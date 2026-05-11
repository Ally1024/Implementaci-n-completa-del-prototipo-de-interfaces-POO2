package com.example.avancesproyecto.ui.theme.Screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.avancesproyecto.navigation.Routes
import com.example.avancesproyecto.ui.theme.VerdeOscuro
import com.example.avancesproyecto.ui.theme.White
import com.example.avancesproyecto.viewmodel.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: EventViewModel
) {

    val registeredEvents by viewModel.registeredEvents

    Scaffold(

        floatingActionButton = {

            FloatingActionButton(

                onClick = {

                    navController.navigate(
                        Routes.ADD_EVENT
                    )
                },

                containerColor = VerdeOscuro,

                contentColor = White
            ) {

                Text(
                    text = "+",

                    fontSize = 30.sp,

                    fontWeight = FontWeight.Bold
                )
            }
        },

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        text = "Green Events UAM"
                    )
                },

                colors = TopAppBarDefaults.topAppBarColors(

                    containerColor = VerdeOscuro,

                    titleContentColor =
                        MaterialTheme.colorScheme.onPrimary
                ),

                actions = {

                    Button(

                        onClick = {

                            navController.navigate(
                                Routes.REGISTERED
                            )
                        },

                        colors = ButtonDefaults.buttonColors(

                            containerColor =
                                MaterialTheme.colorScheme.primary
                        )
                    ) {

                        Text(
                            text = "Mis Eventos"
                        )
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(

            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            items(viewModel.events.value) { event ->

                val isRegistered =
                    registeredEvents.contains(event.id)

                Card(

                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {

                            navController.navigate(
                                "${Routes.DETAIL}/${event.id}"
                            )
                        },

                    elevation =
                        CardDefaults.cardElevation(4.dp)
                ) {

                    Column(

                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(

                            text = event.title,

                            style =
                                MaterialTheme.typography.headlineSmall,

                            color = VerdeOscuro
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(

                            text = event.description,

                            style =
                                MaterialTheme.typography.bodyMedium
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Row(

                            modifier = Modifier.fillMaxWidth(),

                            horizontalArrangement =
                                Arrangement.SpaceBetween
                        ) {

                            Text(

                                text = "📅 ${event.date}",

                                style =
                                    MaterialTheme.typography.bodySmall
                            )

                            Text(

                                text = "📍 ${event.location}",

                                style =
                                    MaterialTheme.typography.bodySmall
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        Text(

                            text = "👥 Máximo 50 estudiantes",

                            color = VerdeOscuro,

                            fontWeight = FontWeight.Bold,

                            fontSize = 13.sp
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        Button(

                            onClick = {

                                if (isRegistered) {

                                    viewModel.unregisterFromEvent(
                                        event.id
                                    )

                                } else {

                                    viewModel.registerForEvent(
                                        event.id
                                    )

                                    navController.navigate(
                                        Routes.INSCRIPCION
                                    )
                                }
                            },

                            modifier =
                                Modifier.align(Alignment.End),

                            colors = ButtonDefaults.buttonColors(

                                containerColor =

                                    if (isRegistered)
                                        MaterialTheme.colorScheme.error
                                    else
                                        VerdeOscuro
                            )
                        ) {

                            Text(

                                if (isRegistered)
                                    "Cancelar Inscripción"
                                else
                                    "Inscribirse"
                            )
                        }
                    }
                }
            }
        }
    }
}