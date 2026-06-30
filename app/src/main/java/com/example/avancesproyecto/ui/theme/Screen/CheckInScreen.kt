package com.example.avancesproyecto.ui.theme.Screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.avancesproyecto.data.local.dao.AsistenciaDao
import com.example.avancesproyecto.data.local.dao.EventDao
import com.example.avancesproyecto.data.local.entity.AsistenciaEntity
import com.example.avancesproyecto.data.local.entity.EventEntity
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInScreen(
    asistenciaDao: AsistenciaDao,
    eventDao: EventDao,
    navController: NavHostController
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Observacion del flujo de datos de eventos locales desde Room convertido a estado de Compose
    val listaEventos: List<EventEntity> by eventDao.getEvents().collectAsState(initial = emptyList())

    // Estados de control para la gestion visual del menu desplegable (Dropdown)
    var expanded by remember { mutableStateOf(false) }
    var eventoSeleccionado by remember { mutableStateOf<EventEntity?>(null) }

    // Carga dinamica y reactiva de los asistentes suscritos en base al evento seleccionado en el menu
    val listaAsistentes: List<AsistenciaEntity> by remember(eventoSeleccionado) {
        if (eventoSeleccionado != null) {
            asistenciaDao.getAsistenciaPorEvento(eventoSeleccionado!!.id)
        } else {
            flowOf(emptyList()) // Retorna un flujo vacio seguro si no se ha elegido ningun evento
        }
    }.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Control de Asistencia") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Seleccione un Evento:",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // ==========================================
            // COMPONENTE: MENU DESPLEGABLE DE EVENTOS
            // ==========================================
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = eventoSeleccionado?.title ?: "Seleccione un evento activo...",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    listaEventos.forEach { evento: EventEntity ->
                        DropdownMenuItem(
                            text = { Text(evento.title) },
                            onClick = {
                                eventoSeleccionado = evento
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Estudiantes Inscritos",
                fontSize = 18.sp,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(10.dp))

            // ==========================================
            // LOGICA DE RENDERIZADO CONDICIONAL DE LA UI
            // ==========================================
            if (eventoSeleccionado == null) {
                // Estado 1: Ningun evento seleccionado en el control
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Por favor, seleccione un evento en la parte superior.", fontSize = 16.sp)
                }
            } else if (listaAsistentes.isEmpty()) {
                // Estado 2: Evento seleccionado pero sin alumnos inscritos
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay estudiantes inscritos en este evento todavía.", fontSize = 16.sp)
                }
            } else {
                // Estado 3: Renderizado de la lista mediante LazyColumn eficiente
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(listaAsistentes, key = { it.id }) { asistente: AsistenciaEntity ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = asistente.nombreEstudiante, fontSize = 18.sp)

                                // Checkbox reactivo que persiste los cambios directamente en Room
                                Checkbox(
                                    checked = asistente.llego,
                                    onCheckedChange = { nuevoEstado ->
                                        // Lanzamiento en hilo secundario para evitar bloqueos en la UI
                                        coroutineScope.launch {
                                            asistenciaDao.actualizarAsistencia(
                                                asistente.copy(llego = nuevoEstado)
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==========================================
            // BOTON: FINALIZAR ACCION DE CONTROL
            // ==========================================
            Button(
                onClick = {
                    if (eventoSeleccionado == null) {
                        Toast.makeText(context, "Por favor seleccione un evento primero", Toast.LENGTH_SHORT).show()
                    } else {
                        // Segmentacion de los estados para la recopilacion del reporte final
                        val presentes = listaAsistentes.filter { it.llego }
                        val ausentes = listaAsistentes.filter { !it.llego }

                        Toast.makeText(
                            context,
                            "Evento [${eventoSeleccionado?.title}]: ${presentes.size} presentes y ${ausentes.size} ausentes.",
                            Toast.LENGTH_LONG
                        ).show()

                        // Retorno seguro hacia el panel administrativo quitando la vista actual
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Finalizar Control de Asistencia", fontSize = 16.sp)
            }
        }
    }
}