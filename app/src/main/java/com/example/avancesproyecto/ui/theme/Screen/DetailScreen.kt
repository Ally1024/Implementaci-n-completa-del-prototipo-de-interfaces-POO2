package com.example.avancesproyecto.ui.theme.Screen

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.avancesproyecto.ui.theme.VerdeOscuro
import com.example.avancesproyecto.ui.theme.White
import com.example.avancesproyecto.viewmodel.EventViewModel
import com.example.avancesproyecto.viewmodel.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(id: Int, viewModel: EventViewModel, navController: NavHostController) {
    val event         = viewModel.getEventById(id)
    val registrations by viewModel.registrations
    val context       = LocalContext.current
    val userId        = SessionManager.currentUser?.id ?: -1
    val isReg         = registrations[id]?.contains(userId) == true

    val bitmap = remember(event?.imageUri) {
        event?.imageUri?.let {
            try {
                context.contentResolver.openInputStream(Uri.parse(it))?.use { s ->
                    BitmapFactory.decodeStream(s)?.asImageBitmap()
                }
            } catch (e: Exception) { null }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title          = { Text(event?.title ?: "Evento") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = VerdeOscuro, titleContentColor = White)
            )
        }
    ) { padding ->
        if (event == null) {
            Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) { Text("Evento no encontrado.") }
            return@Scaffold
        }

        Column(Modifier.fillMaxSize().padding(padding)) {
            // Foto del evento
            if (bitmap != null) {
                Image(bitmap, "Foto del evento", Modifier.fillMaxWidth().height(200.dp),
                    contentScale = ContentScale.Crop)
            }

            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(event.title, style = MaterialTheme.typography.headlineSmall,
                    color = VerdeOscuro, fontWeight = FontWeight.Bold)
                Text(event.description, style = MaterialTheme.typography.bodyMedium)

                HorizontalDivider()
                Text("📅 ${event.date}   🕐 ${event.time}", style = MaterialTheme.typography.bodyMedium)

                HorizontalDivider()
                Text("📍 Ubicación:", fontWeight = FontWeight.Bold, color = VerdeOscuro)
                Text(event.location, style = MaterialTheme.typography.bodyMedium)

                // Botón Google Maps
                Button(
                    onClick = {
                        val q      = Uri.encode(event.location)
                        val uri    = Uri.parse("https://www.google.com/maps/search/?api=1&query=$q")
                        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                            setPackage("com.google.android.apps.maps")
                        }
                        if (intent.resolveActivity(context.packageManager) != null)
                            context.startActivity(intent)
                        else context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(12.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) { Text("🗺️ Ver en Google Maps") }

                Text("👥 Cupo máximo: ${event.maxCapacity} estudiantes", color = VerdeOscuro)

                // Inscribirse / Cancelar (solo si hay alumno logueado)
                if (userId >= 0) {
                    Button(
                        onClick = {
                            if (isReg) viewModel.unregisterFromEvent(id, userId)
                            else       viewModel.registerForEvent(id, userId)
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape    = RoundedCornerShape(50.dp),
                        colors   = ButtonDefaults.buttonColors(
                            containerColor = if (isReg) MaterialTheme.colorScheme.error else VerdeOscuro
                        )
                    ) {
                        Text(if (isReg) "❌ Cancelar Inscripción" else "✅ Inscribirse al Evento",
                            fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
