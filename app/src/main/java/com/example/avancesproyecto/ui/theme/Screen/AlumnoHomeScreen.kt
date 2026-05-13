package com.example.avancesproyecto.ui.theme.Screen

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.avancesproyecto.navigation.Routes
import com.example.avancesproyecto.ui.theme.VerdeOscuro
import com.example.avancesproyecto.ui.theme.White
import com.example.avancesproyecto.viewmodel.EventViewModel
import com.example.avancesproyecto.viewmodel.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlumnoHomeScreen(
    navController:  NavHostController,
    eventViewModel: EventViewModel,
    onLogout:       () -> Unit
) {
    val user          = SessionManager.currentUser
    val registrations by eventViewModel.registrations
    val context       = LocalContext.current
    val events        = eventViewModel.events

    Scaffold(
        topBar = {
            TopAppBar(
                title  = { Text("Green Events UAM") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VerdeOscuro, titleContentColor = White),
                actions = {
                    TextButton(onClick = { navController.navigate(Routes.PERFIL) }) {
                        Text("👤 Perfil", color = White)
                    }
                    TextButton(onClick = onLogout) {
                        Text("Salir", color = White)
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Hola, ${user?.name?.split(" ")?.first() ?: "Alumno"} 👋",
                    fontSize = 18.sp, color = VerdeOscuro, fontWeight = FontWeight.Bold)
                Text("Explora y regístrate en los eventos disponibles", fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.outline)
                Spacer(Modifier.height(4.dp))
            }

            items(events) { event ->
                val userId     = user?.id ?: -1
                val isReg      = registrations[event.id]?.contains(userId) == true

                // Imagen del evento si existe
                val bitmap = remember(event.imageUri) {
                    event.imageUri?.let {
                        try {
                            context.contentResolver.openInputStream(Uri.parse(it))?.use { s ->
                                BitmapFactory.decodeStream(s)?.asImageBitmap()
                            }
                        } catch (e: Exception) { null }
                    }
                }

                Card(
                    Modifier.fillMaxWidth().clickable {
                        navController.navigate("${Routes.DETAIL}/${event.id}")
                    },
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape     = RoundedCornerShape(14.dp)
                ) {
                    Column {
                        // Foto si tiene
                        if (bitmap != null) {
                            Image(bitmap, contentDescription = "Foto",
                                Modifier.fillMaxWidth().height(150.dp),
                                contentScale = ContentScale.Crop)
                        }

                        Column(Modifier.padding(14.dp)) {
                            Text(event.title, style = MaterialTheme.typography.titleMedium,
                                color = VerdeOscuro, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                            Text(event.description, style = MaterialTheme.typography.bodySmall,
                                maxLines = 2)
                            Spacer(Modifier.height(8.dp))
                            Row(Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("📅 ${event.date}  🕐 ${event.time}",
                                    style = MaterialTheme.typography.bodySmall)
                                Text("📍 ${event.location.take(25)}…",
                                    style = MaterialTheme.typography.bodySmall)
                            }

                            Spacer(Modifier.height(10.dp))

                            Row(Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                                // Botón INSCRIBIRSE / CANCELAR
                                Button(
                                    onClick = {
                                        if (userId >= 0) {
                                            if (isReg) eventViewModel.unregisterFromEvent(event.id, userId)
                                            else       eventViewModel.registerForEvent(event.id, userId)
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape    = RoundedCornerShape(10.dp),
                                    colors   = ButtonDefaults.buttonColors(
                                        containerColor = if (isReg)
                                            MaterialTheme.colorScheme.error else VerdeOscuro
                                    )
                                ) {
                                    Text(if (isReg) "Cancelar" else "Inscribirse",
                                        fontSize = 13.sp)
                                }

                                // Botón LLEGAR → abre Google Maps con dirección real
                                Button(
                                    onClick = {
                                        val query   = Uri.encode(event.location)
                                        val mapsUri = Uri.parse(
                                            "https://www.google.com/maps/search/?api=1&query=$query")
                                        val intent  = Intent(Intent.ACTION_VIEW, mapsUri).apply {
                                            setPackage("com.google.android.apps.maps")
                                        }
                                        if (intent.resolveActivity(context.packageManager) != null)
                                            context.startActivity(intent)
                                        else
                                            context.startActivity(Intent(Intent.ACTION_VIEW, mapsUri))
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape    = RoundedCornerShape(10.dp),
                                    colors   = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                ) { Text("🗺️ Llegar", fontSize = 13.sp) }
                            }
                        }
                    }
                }
            }
        }
    }
}
