package com.example.avancesproyecto.ui.theme.Screen

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.avancesproyecto.ui.theme.GrisClaro
import com.example.avancesproyecto.ui.theme.VerdeOscuro
import com.example.avancesproyecto.ui.theme.White
import com.example.avancesproyecto.viewmodel.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCrearEventoScreen(navController: NavHostController, viewModel: EventViewModel) {
    var titulo      by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fecha       by remember { mutableStateOf("") }
    var hora        by remember { mutableStateOf("") }
    var ubicacion   by remember { mutableStateOf("") }
    var imageUri    by remember { mutableStateOf<String?>(null) }
    var errorMsg    by remember { mutableStateOf("") }
    var guardado    by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val imageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imageUri = uri?.toString() }

    // Mostrar la imagen seleccionada
    val bitmap = remember(imageUri) {
        imageUri?.let {
            try {
                context.contentResolver.openInputStream(Uri.parse(it))?.use { stream ->
                    BitmapFactory.decodeStream(stream)?.asImageBitmap()
                }
            } catch (e: Exception) { null }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title              = { Text("Crear Evento") },
                navigationIcon     = {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("← Atrás", color = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VerdeOscuro, titleContentColor = White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize().padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (guardado) {
                Card(colors = CardDefaults.cardColors(containerColor = VerdeOscuro)) {
                    Text("✅ Evento creado exitosamente.", color = White,
                        modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold)
                }
            }

            val fieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = VerdeOscuro, unfocusedBorderColor = GrisClaro,
                focusedLabelColor    = VerdeOscuro, unfocusedLabelColor  = VerdeOscuro,
                cursorColor          = VerdeOscuro
            )
            val textStyle = TextStyle(color = VerdeOscuro)

            OutlinedTextField(titulo, { titulo = it; guardado = false },
                label = { Text("Título del evento") }, modifier = Modifier.fillMaxWidth(),
                textStyle = textStyle, colors = fieldColors)

            OutlinedTextField(descripcion, { descripcion = it; guardado = false },
                label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth(),
                minLines = 3, textStyle = textStyle, colors = fieldColors)

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(fecha, { fecha = it; guardado = false },
                    label = { Text("Fecha (AAAA-MM-DD)") },
                    modifier = Modifier.weight(1f), textStyle = textStyle, colors = fieldColors)
                OutlinedTextField(hora, { hora = it; guardado = false },
                    label = { Text("Hora (HH:MM)") },
                    modifier = Modifier.weight(1f), textStyle = textStyle, colors = fieldColors)
            }

            OutlinedTextField(ubicacion, { ubicacion = it; guardado = false },
                label       = { Text("Ubicación (dirección completa)") },
                placeholder = { Text("Ej: UAM Managua, Nicaragua") },
                modifier    = Modifier.fillMaxWidth(), textStyle = textStyle, colors = fieldColors)

            // Botón ver en Maps (preview)
            if (ubicacion.isNotBlank()) {
                OutlinedButton(
                    onClick = {
                        val uri    = Uri.encode(ubicacion)
                        val intent = Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.google.com/maps/search/?api=1&query=$uri"))
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = VerdeOscuro)
                ) { Text("🗺️ Verificar ubicación en Maps") }
            }

            // Foto del evento
            Button(
                onClick = { imageLauncher.launch("image/*") },
                colors  = ButtonDefaults.buttonColors(containerColor = GrisClaro, contentColor = VerdeOscuro),
                modifier = Modifier.fillMaxWidth()
            ) { Text("📷 Seleccionar foto del evento") }

            if (bitmap != null) {
                Image(
                    bitmap             = bitmap,
                    contentDescription = "Foto seleccionada",
                    modifier           = Modifier.fillMaxWidth().height(180.dp),
                    contentScale       = ContentScale.Crop
                )
            } else if (imageUri != null) {
                Text("✅ Foto seleccionada", color = VerdeOscuro)
            }

            if (errorMsg.isNotEmpty())
                Text(errorMsg, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)

            Button(
                onClick = {
                    when {
                        titulo.isBlank()    -> errorMsg = "El título es obligatorio."
                        descripcion.isBlank()-> errorMsg = "La descripción es obligatoria."
                        fecha.isBlank()     -> errorMsg = "La fecha es obligatoria."
                        hora.isBlank()      -> errorMsg = "La hora es obligatoria."
                        ubicacion.isBlank() -> errorMsg = "La ubicación es obligatoria."
                        else -> {
                            viewModel.addEvent(titulo, descripcion, fecha, hora, ubicacion, imageUri)
                            titulo = ""; descripcion = ""; fecha = ""; hora = ""; ubicacion = ""
                            imageUri = null; errorMsg = ""; guardado = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(50.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = VerdeOscuro)
            ) { Text("Guardar Evento", fontWeight = FontWeight.Bold, fontSize = 16.sp) }
        }
    }
}
