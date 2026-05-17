package com.example.avancesproyecto.ui.theme.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.avancesproyecto.ui.theme.VerdeOscuro
import com.example.avancesproyecto.viewmodel.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteEventScreen(
    navController: NavHostController,
    viewModel: EventViewModel
) {

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
                                viewModel.deleteEvent(event.id)
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
    }
}