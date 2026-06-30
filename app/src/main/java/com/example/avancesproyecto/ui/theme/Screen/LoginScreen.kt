package com.example.avancesproyecto.ui.theme.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.avancesproyecto.R
import com.example.avancesproyecto.ui.theme.GrisClaro
import com.example.avancesproyecto.ui.theme.VerdeOscuro
import com.example.avancesproyecto.ui.theme.White
import com.example.avancesproyecto.viewmodel.UserViewModel

@Composable
fun LoginScreen(
    // Asignación por defecto del ViewModel para desacoplamiento de arquitectura
    viewModel: UserViewModel = viewModel(),
    onLoginClick: (Boolean) -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {

    // Estados mutables para el control del formulario y visibilidad de contraseña
    var cif by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var cifError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var isAdmin by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Contenedor circular para el logo institucional de la universidad
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .background(color = White, shape = CircleShape)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.uam_verde),
                    contentDescription = "Logo UAM Verde",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Green Events",
                fontSize = 28.sp,
                color = VerdeOscuro,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "UAM VERDE",
                fontSize = 16.sp,
                color = VerdeOscuro
            )

            Spacer(modifier = Modifier.height(30.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 420.dp),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {

                Column(modifier = Modifier.padding(22.dp)) {

                    Text(
                        text = "Inicio de sesión",
                        fontSize = 22.sp,
                        color = VerdeOscuro,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // ==========================================
                    // CAMPO: CIF (IDENTIFICACIÓN)
                    // ==========================================
                    OutlinedTextField(
                        value = cif,
                        onValueChange = { input ->
                            // Regla de negocio: Máximo 8 caracteres puramente numéricos
                            if (input.length <= 8 && input.all { char -> char.isDigit() }) {
                                cif = input
                            }
                            cifError = ""
                        },
                        label = { Text("CIF") },
                        leadingIcon = {
                            Icon(Icons.Default.Badge, contentDescription = null, tint = VerdeOscuro)
                        },
                        singleLine = true,
                        isError = cifError.isNotEmpty(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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

                    if (cifError.isNotEmpty()) {
                        Text(
                            text = cifError,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // ==========================================
                    // CAMPO: CONTRASEÑA
                    // ==========================================
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = ""
                        },
                        label = { Text("Contraseña") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = VerdeOscuro)
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Mostrar contraseña",
                                    tint = VerdeOscuro
                                )
                            }
                        },
                        singleLine = true,
                        isError = passwordError.isNotEmpty(),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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

                    if (passwordError.isNotEmpty()) {
                        Text(
                            text = passwordError,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // ==========================================
                    // BOTÓN: INGRESO Y LOGUEO DE CREDENCIALES
                    // ==========================================
                    Button(
                        onClick = {
                            // Estructura de validación secuencial antes de llamadas de red/base de datos
                            when {
                                cif.isBlank() -> { cifError = "Ingrese su CIF" }
                                cif.length != 8 -> { cifError = "El CIF debe tener 8 números" }
                                password.isBlank() -> { passwordError = "Ingrese su contraseña" }
                                password.length < 6 -> { passwordError = "La contraseña debe tener mínimo 6 caracteres" }
                                else -> {
                                    // Validación del rol y verificación de estado en persistencia local
                                    if (!isAdmin) {
                                        val user = viewModel.loginUser(cif)
                                        if (user == null) {
                                            cifError = "El CIF no está registrado"
                                        } else if (user.isBlocked) {
                                            cifError = "Este usuario se encuentra bloqueado"
                                        } else {
                                            onLoginClick(false)
                                        }
                                    } else {
                                        // Acceso administrativo directo temporal
                                        onLoginClick(true)
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VerdeOscuro,
                            contentColor = White
                        )
                    ) {
                        Text(
                            text = "Ingresar",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "Ingresar como",
                        color = VerdeOscuro,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Selector de roles institucionales (Estudiante vs Administrador)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedButton(
                            onClick = { isAdmin = false },
                            shape = RoundedCornerShape(50.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (!isAdmin) VerdeOscuro.copy(alpha = 0.1f) else White
                            )
                        ) {
                            Text("Estudiante", color = VerdeOscuro)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        OutlinedButton(
                            onClick = { isAdmin = true },
                            shape = RoundedCornerShape(50.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isAdmin) VerdeOscuro.copy(alpha = 0.1f) else White
                            )
                        ) {
                            Text("Administrador", color = VerdeOscuro)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Navegación hacia la pantalla de registro
                    TextButton(
                        onClick = { onRegisterClick() },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "¿No tienes cuenta? Regístrate",
                            color = VerdeOscuro,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}