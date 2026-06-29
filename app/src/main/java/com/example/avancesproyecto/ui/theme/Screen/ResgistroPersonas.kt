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
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.avancesproyecto.R
import com.example.avancesproyecto.ui.theme.GrisClaro
import com.example.avancesproyecto.ui.theme.VerdeOscuro
import com.example.avancesproyecto.ui.theme.White
import com.example.avancesproyecto.viewmodel.UserViewModel
import com.example.avancesproyecto.model.UserType

@Composable
fun RegisterScreen(
    viewModel: UserViewModel? = null,
    onRegisterClick: () -> Unit = {},
    onBackToLogin: () -> Unit = {}
) {

    var nombre by rememberSaveable { mutableStateOf("") }
    var cif by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var nombreError by remember { mutableStateOf("") }
    var cifError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }

    // NUEVO: Limpiar el error de duplicados del ViewModel cada vez que entramos a esta pantalla
    LaunchedEffect(Unit) {
        viewModel?.registrationError = null
    }

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

            Box(
                modifier = Modifier
                    .size(140.dp)
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

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Crear cuenta",
                fontSize = 28.sp,
                color = VerdeOscuro,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Green Events - UAM VERDE",
                fontSize = 16.sp,
                color = VerdeOscuro
            )

            Spacer(modifier = Modifier.height(28.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 420.dp),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(22.dp)) {

                    // =========================================================================
                    //  NUEVO: CUADRO DE AVISO SI EL BACKEND DEVUELVE DUPLICADO O ERROR
                    // =========================================================================
                    viewModel?.registrationError?.let { msg ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = msg,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(12.dp),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // NOMBRE COMPLETO
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = {
                            nombre = it
                            nombreError = ""
                            viewModel?.registrationError = null // Limpia error general al escribir
                        },
                        label = { Text("Nombre completo") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = VerdeOscuro) },
                        singleLine = true,
                        isError = nombreError.isNotEmpty(),
                        textStyle = TextStyle(color = VerdeOscuro),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = VerdeOscuro, unfocusedTextColor = VerdeOscuro,
                            focusedBorderColor = VerdeOscuro, unfocusedBorderColor = GrisClaro,
                            focusedLabelColor = VerdeOscuro, unfocusedLabelColor = VerdeOscuro, cursorColor = VerdeOscuro
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (nombreError.isNotEmpty()) {
                        Text(text = nombreError, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // CIF
                    OutlinedTextField(
                        value = cif,
                        onValueChange = {
                            if (it.length <= 8 && it.all { char -> char.isDigit() }) {
                                cif = it
                            }
                            cifError = ""
                            viewModel?.registrationError = null // Limpia error general al escribir
                        },
                        label = { Text("CIF") },
                        leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = VerdeOscuro) },
                        singleLine = true,
                        isError = cifError.isNotEmpty(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = TextStyle(color = VerdeOscuro),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = VerdeOscuro, unfocusedTextColor = VerdeOscuro,
                            focusedBorderColor = VerdeOscuro, unfocusedBorderColor = GrisClaro,
                            focusedLabelColor = VerdeOscuro, unfocusedLabelColor = VerdeOscuro, cursorColor = VerdeOscuro
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (cifError.isNotEmpty()) {
                        Text(text = cifError, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // CONTRASEÑA
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = ""
                        },
                        label = { Text("Contraseña") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = VerdeOscuro) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null, tint = VerdeOscuro
                                )
                            }
                        },
                        singleLine = true,
                        isError = passwordError.isNotEmpty(),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        textStyle = TextStyle(color = VerdeOscuro),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = VerdeOscuro, unfocusedTextColor = VerdeOscuro,
                            focusedBorderColor = VerdeOscuro, unfocusedBorderColor = GrisClaro,
                            focusedLabelColor = VerdeOscuro, unfocusedLabelColor = VerdeOscuro, cursorColor = VerdeOscuro
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (passwordError.isNotEmpty()) {
                        Text(text = passwordError, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // CONFIRMAR CONTRASEÑA
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            confirmPasswordError = ""
                        },
                        label = { Text("Confirmar contraseña") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = VerdeOscuro) },
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(
                                    imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null, tint = VerdeOscuro
                                )
                            }
                        },
                        singleLine = true,
                        isError = confirmPasswordError.isNotEmpty(),
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        textStyle = TextStyle(color = VerdeOscuro),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = VerdeOscuro, unfocusedTextColor = VerdeOscuro,
                            focusedBorderColor = VerdeOscuro, unfocusedBorderColor = GrisClaro,
                            focusedLabelColor = VerdeOscuro, unfocusedLabelColor = VerdeOscuro, cursorColor = VerdeOscuro
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (confirmPasswordError.isNotEmpty()) {
                        Text(text = confirmPasswordError, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // BOTÓN REGISTRARSE
                    Button(
                        onClick = {
                            when {
                                nombre.isBlank() -> { nombreError = "Ingrese su nombre" }
                                cif.isBlank() -> { cifError = "Ingrese su CIF" }
                                cif.length != 8 -> { cifError = "El CIF debe tener 8 números" }
                                password.isBlank() -> { passwordError = "Ingrese una contraseña" }
                                password.length < 6 -> { passwordError = "La contraseña debe tener mínimo 6 caracteres" }
                                confirmPassword != password -> { confirmPasswordError = "Las contraseñas no coinciden" }
                                else -> {
                                    // 🛠️ MODIFICADO: Pasamos el lambda onSuccess de la función addUser modificada
                                    viewModel?.addUser(
                                        name = nombre,
                                        cif = cif,
                                        userType = UserType.ESTUDIANTE,
                                        onSuccess = {
                                            // Solo cambia de pantalla si el backend dice que todo estuvo bien (200 OK)
                                            onRegisterClick()
                                        }
                                    )
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VerdeOscuro, contentColor = White)
                    ) {
                        Text(text = "Registrarse", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(
                        onClick = { onBackToLogin() },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(text = "Volver al inicio de sesión", color = VerdeOscuro, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    RegisterScreen()
}