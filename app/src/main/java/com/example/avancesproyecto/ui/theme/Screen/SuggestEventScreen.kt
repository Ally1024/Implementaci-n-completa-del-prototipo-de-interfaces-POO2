package com.example.avancesproyecto.ui.theme.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.avancesproyecto.ui.theme.GrisClaro
import com.example.avancesproyecto.ui.theme.VerdeOscuro
import com.example.avancesproyecto.ui.theme.White
import com.example.avancesproyecto.viewmodel.SuggestionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestEventScreen(
    navController: NavHostController,
    suggestionViewModel: SuggestionViewModel
) {

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var locacion by remember { mutableStateOf("") }
    var capacidad by remember { mutableStateOf("") }
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


        OutlinedTextField(

            value = nombre,

            onValueChange = {
                nombre = it
            },

            label = {
                Text("Nombre")
            },

            textStyle = TextStyle(
                color = VerdeOscuro
            ),

            colors = OutlinedTextFieldDefaults.colors(

                focusedTextColor = VerdeOscuro,
                unfocusedTextColor = VerdeOscuro,

                focusedBorderColor = VerdeOscuro,
                unfocusedBorderColor = GrisClaro,

                cursorColor = VerdeOscuro
            ),

            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(

            value = descripcion,

            onValueChange = {
                descripcion = it
            },

            label = {
                Text("Descripción")
            },

            textStyle = TextStyle(
                color = VerdeOscuro
            ),

            colors = OutlinedTextFieldDefaults.colors(

                focusedTextColor = VerdeOscuro,
                unfocusedTextColor = VerdeOscuro,

                focusedBorderColor = VerdeOscuro,
                unfocusedBorderColor = GrisClaro,

                cursorColor = VerdeOscuro
            ),

            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(

            value = fecha,

            onValueChange = {
                fecha = it
            },

            label = {
                Text("Fecha")
            },

            textStyle = TextStyle(
                color = VerdeOscuro
            ),

            colors = OutlinedTextFieldDefaults.colors(

                focusedTextColor = VerdeOscuro,
                unfocusedTextColor = VerdeOscuro,

                focusedBorderColor = VerdeOscuro,
                unfocusedBorderColor = GrisClaro,

                cursorColor = VerdeOscuro
            ),

            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(

            value = locacion,

            onValueChange = {
                locacion = it
            },

            label = {
                Text("Locación")
            },

            textStyle = TextStyle(
                color = VerdeOscuro
            ),

            colors = OutlinedTextFieldDefaults.colors(

                focusedTextColor = VerdeOscuro,
                unfocusedTextColor = VerdeOscuro,

                focusedBorderColor = VerdeOscuro,
                unfocusedBorderColor = GrisClaro,

                cursorColor = VerdeOscuro
            ),

            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(

            value = capacidad,

            onValueChange = {
                capacidad = it
            },

            label = {
                Text("Capacidad")
            },

            textStyle = TextStyle(
                color = VerdeOscuro
            ),

            colors = OutlinedTextFieldDefaults.colors(

                focusedTextColor = VerdeOscuro,
                unfocusedTextColor = VerdeOscuro,

                focusedBorderColor = VerdeOscuro,
                unfocusedBorderColor = GrisClaro,

                cursorColor = VerdeOscuro
            ),

            modifier = Modifier.fillMaxWidth()
        )

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

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(

            onClick = {

                suggestionViewModel.addSuggestion(
                    title = nombre,
                    description = descripcion,
                    date = fecha,
                    location = locacion,
                    capacity = capacidad.toIntOrNull() ?: 0,
                    onSuccess = {
                        navController.popBackStack()
                    },
                    onError = { error ->
                        errorMessage = error
                        showErrorMessage = true
                    }
                )
            },

            colors = ButtonDefaults.buttonColors(
                containerColor = VerdeOscuro,
                contentColor = White
            ),

            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Enviar sugerencia")
        }
    }
    }
}