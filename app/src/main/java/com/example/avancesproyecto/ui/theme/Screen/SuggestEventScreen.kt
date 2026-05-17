package com.example.avancesproyecto.ui.theme.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.avancesproyecto.viewmodel.EventViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun SuggestEventScreen(
    navController: NavHostController,
    viewModel: EventViewModel
) {

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var locacion by remember { mutableStateOf("") }
    var capacidad by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {

        Text("Sugerir Evento")

        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
        OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") })
        OutlinedTextField(value = fecha, onValueChange = { fecha = it }, label = { Text("Fecha") })
        OutlinedTextField(value = locacion, onValueChange = { locacion = it }, label = { Text("Locación") })
        OutlinedTextField(value = capacidad, onValueChange = { capacidad = it }, label = { Text("Capacidad") })

        Button(onClick = {
            viewModel.addSuggestion(
                nombre,
                descripcion,
                fecha,
                locacion,
                capacidad.toIntOrNull() ?: 0
            )

            navController.popBackStack()
        }) {
            Text("Enviar sugerencia")
        }
    }
}