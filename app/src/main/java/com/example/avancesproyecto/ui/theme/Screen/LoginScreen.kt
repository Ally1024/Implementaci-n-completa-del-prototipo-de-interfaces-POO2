package com.example.avancesproyecto.ui.theme.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.avancesproyecto.R
import com.example.avancesproyecto.ui.theme.GrisClaro
import com.example.avancesproyecto.ui.theme.VerdeOscuro
import com.example.avancesproyecto.ui.theme.White
import com.example.avancesproyecto.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onAdminLogin:  () -> Unit,
    onUserLogin:   () -> Unit
) {
    var isAdminTab      by rememberSaveable { mutableStateOf(false) }
    var cif             by rememberSaveable { mutableStateOf("") }
    var password        by rememberSaveable { mutableStateOf("") }
    var passwordVisible by remember        { mutableStateOf(false) }
    var errorMsg        by remember        { mutableStateOf("") }

    // Reglas de CIF:
    // Admin  → solo dígitos, sin límite de cantidad
    // Alumno → solo dígitos, exactamente 8 caracteres
    val maxCifLength = if (isAdminTab) Int.MAX_VALUE else 8

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(VerdeOscuro, White)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement   = Arrangement.Center,
            horizontalAlignment   = Alignment.CenterHorizontally
        ) {
            // Logo
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .background(color = White, shape = CircleShape)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter            = painterResource(id = R.drawable.uam_verde),
                    contentDescription = "Logo UAM Verde",
                    modifier           = Modifier.fillMaxSize(),
                    contentScale       = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(18.dp))
            Text("UAM X Green Events", fontSize = 26.sp, color = White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(20.dp))

            // Selector Admin / Alumno
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth(0.85f)) {
                SegmentedButton(
                    shape    = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                    onClick  = { isAdminTab = false; cif = ""; errorMsg = "" },
                    selected = !isAdminTab
                ) { Text("👤 Alumno") }

                SegmentedButton(
                    shape    = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                    onClick  = { isAdminTab = true; cif = ""; errorMsg = "" },
                    selected = isAdminTab
                ) { Text("🛠️ Admin") }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta de login
            Card(
                modifier  = Modifier.fillMaxWidth().widthIn(max = 420.dp),
                shape     = RoundedCornerShape(22.dp),
                colors    = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(22.dp)) {

                    Text(
                        text       = if (isAdminTab) "Acceso Administrador" else "Inicio de sesión",
                        fontSize   = 20.sp,
                        color      = VerdeOscuro,
                        fontWeight = FontWeight.Bold,
                        textAlign  = TextAlign.Center,
                        modifier   = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // ── Campo CIF ──────────────────────────────────
                    OutlinedTextField(
                        value         = cif,
                        onValueChange = { input ->
                            // Solo aceptar dígitos
                            val soloDigitos = input.filter { it.isDigit() }
                            // Limitar a 8 si es alumno
                            cif = if (isAdminTab) soloDigitos
                                  else soloDigitos.take(8)
                            errorMsg = ""
                        },
                        label         = {
                            Text(
                                if (isAdminTab) "CIF Administrador (solo números)"
                                else            "CIF Estudiante (8 dígitos)"
                            )
                        },
                        leadingIcon   = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = VerdeOscuro)
                        },
                        // Mostrar contador solo para alumno
                        supportingText = if (!isAdminTab) {
                            { Text("${cif.length}/8 dígitos",
                                color = if (cif.length == 8) VerdeOscuro
                                        else MaterialTheme.colorScheme.outline) }
                        } else null,
                        isError        = !isAdminTab && cif.isNotEmpty() && cif.length != 8,
                        singleLine     = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle      = TextStyle(color = VerdeOscuro),
                        colors         = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor   = VerdeOscuro,
                            unfocusedBorderColor = GrisClaro,
                            focusedLabelColor    = VerdeOscuro,
                            unfocusedLabelColor  = VerdeOscuro,
                            cursorColor          = VerdeOscuro
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // ── Campo Contraseña ───────────────────────────
                    OutlinedTextField(
                        value         = password,
                        onValueChange = { password = it; errorMsg = "" },
                        label         = { Text("Contraseña") },
                        leadingIcon   = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = VerdeOscuro)
                        },
                        trailingIcon  = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector        = if (passwordVisible) Icons.Default.Visibility
                                                         else                 Icons.Default.VisibilityOff,
                                    contentDescription = "Mostrar contraseña",
                                    tint               = VerdeOscuro
                                )
                            }
                        },
                        singleLine            = true,
                        visualTransformation  = if (passwordVisible) VisualTransformation.None
                                                else                 PasswordVisualTransformation(),
                        keyboardOptions       = KeyboardOptions(keyboardType = KeyboardType.Password),
                        textStyle             = TextStyle(color = VerdeOscuro),
                        colors                = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor   = VerdeOscuro,
                            unfocusedBorderColor = GrisClaro,
                            focusedLabelColor    = VerdeOscuro,
                            unfocusedLabelColor  = VerdeOscuro,
                            cursorColor          = VerdeOscuro
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (errorMsg.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(errorMsg, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    // ── Botón ingresar ─────────────────────────────
                    Button(
                        onClick = {
                            when {
                                cif.isBlank() -> {
                                    errorMsg = "El CIF es obligatorio."
                                }
                                !cif.all { it.isDigit() } -> {
                                    // Nunca debería llegar aquí por el filtro, pero por si acaso
                                    errorMsg = "El CIF solo puede contener números."
                                }
                                !isAdminTab && cif.length != 8 -> {
                                    errorMsg = "El CIF del alumno debe tener exactamente 8 dígitos."
                                }
                                password.isBlank() -> {
                                    errorMsg = "La contraseña es obligatoria."
                                }
                                isAdminTab -> {
                                    if (authViewModel.loginAdmin(cif, password)) onAdminLogin()
                                    else errorMsg = "CIF o contraseña de administrador incorrectos."
                                }
                                else -> {
                                    if (authViewModel.loginUser(cif, password)) onUserLogin()
                                    else errorMsg = "CIF o contraseña incorrectos."
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape    = RoundedCornerShape(50.dp),
                        colors   = ButtonDefaults.buttonColors(
                            containerColor = VerdeOscuro, contentColor = White
                        )
                    ) {
                        Text("Iniciar sesión", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    // Hint credenciales de prueba
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text      = if (isAdminTab)
                            "Admin: 12345678901 / admin123"
                        else
                            "Prueba: 20240001 / pass123",
                        fontSize  = 11.sp,
                        color     = MaterialTheme.colorScheme.outline,
                        textAlign = TextAlign.Center,
                        modifier  = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
