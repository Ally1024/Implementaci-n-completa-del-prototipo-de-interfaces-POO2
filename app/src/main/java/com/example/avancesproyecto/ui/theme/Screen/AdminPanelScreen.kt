package com.example.avancesproyecto.ui.theme.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun AdminPanelScreen(
    navController: NavHostController,
    viewModel: EventViewModel
) {

    Scaffold(

        topBar = {
            TopAppBar(

                title = {
                    Text("Panel Administrador")
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VerdeOscuro,
                    titleContentColor = White
                )
            )
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                text = "Gestión de Eventos",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeOscuro
            )

            Spacer(modifier = Modifier.height(20.dp))

            // AGREGAR EVENTO
            Button(

                onClick = {
                    navController.navigate(Routes.ADD_EVENT)
                },

                modifier = Modifier.fillMaxWidth(),

                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeOscuro
                )

            ) {
                Text("Agregar Evento")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ELIMINAR EVENTO
            Button(

                onClick = {
                    navController.navigate(Routes.DELETE_EVENT)
                },

                modifier = Modifier.fillMaxWidth(),

                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )

            ) {
                Text("Eliminar Eventos")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Sugerencias de estudiantes",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeOscuro
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(viewModel.suggestions) { suggestion ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = suggestion.title,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(suggestion.description)

                            Spacer(modifier = Modifier.height(10.dp))

                            Row {

                                Button(
                                    onClick = {
                                        viewModel.approveSuggestion(suggestion)
                                    }
                                ) {
                                    Text("Aprobar")
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(

                                    onClick = {
                                        viewModel.rejectSuggestion(suggestion)
                                    },

                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.error
                                    )

                                ) {
                                    Text("Rechazar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}