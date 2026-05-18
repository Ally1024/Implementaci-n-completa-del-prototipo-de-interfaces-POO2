package com.example.avancesproyecto.ui.theme.Screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.avancesproyecto.R
import com.example.avancesproyecto.navigation.Routes
import com.example.avancesproyecto.ui.theme.GrisClaro
import com.example.avancesproyecto.ui.theme.VerdeOscuro
import com.example.avancesproyecto.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InscripcionScreen(
    navController: NavHostController
) {

    var cif by remember { mutableStateOf("") }
    var carrera by remember { mutableStateOf("") }
    var beneficio by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Información") },
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(VerdeOscuro, White)
                    )
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .background(color = White, shape = CircleShape)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.uam_verde),
                        contentDescription = "Logo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Formulario de Inscripción",
                    fontSize = 28.sp,
                    color = White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(30.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                ) {

                    Column(modifier = Modifier.padding(22.dp)) {

                        OutlinedTextField(
                            value = cif,
                            onValueChange = { cif = it },
                            label = { Text("CIF") },
                            textStyle = TextStyle(color = VerdeOscuro),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = VerdeOscuro,
                                unfocusedTextColor = VerdeOscuro,
                                focusedBorderColor = VerdeOscuro,
                                unfocusedBorderColor = GrisClaro,
                                focusedLabelColor = VerdeOscuro,
                                unfocusedLabelColor = VerdeOscuro,
                                cursorColor = VerdeOscuro
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = carrera,
                            onValueChange = { carrera = it },
                            label = { Text("Carrera") },
                            textStyle = TextStyle(color = VerdeOscuro),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = VerdeOscuro,
                                unfocusedTextColor = VerdeOscuro,
                                focusedBorderColor = VerdeOscuro,
                                unfocusedBorderColor = GrisClaro,
                                focusedLabelColor = VerdeOscuro,
                                unfocusedLabelColor = VerdeOscuro,
                                cursorColor = VerdeOscuro
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = beneficio,
                            onValueChange = { beneficio = it },
                            label = { Text("Beneficio") },
                            textStyle = TextStyle(color = VerdeOscuro),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = VerdeOscuro,
                                unfocusedTextColor = VerdeOscuro,
                                focusedBorderColor = VerdeOscuro,
                                unfocusedBorderColor = GrisClaro,
                                focusedLabelColor = VerdeOscuro,
                                unfocusedLabelColor = VerdeOscuro,
                                cursorColor = VerdeOscuro
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(28.dp))

                        Button(
                            onClick = {
                                Toast.makeText(
                                    context,
                                    "Inscripción realizada correctamente",
                                    Toast.LENGTH_LONG
                                ).show()

                                navController.navigate(Routes.REGISTERED) {
                                    popUpTo(Routes.HOME) { inclusive = false }
                                    launchSingleTop = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(55.dp),
                            shape = RoundedCornerShape(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = VerdeOscuro,
                                contentColor = White
                            )
                        ) {
                            Text(
                                text = "Confirmar inscripción",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
