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
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.avancesproyecto.R
import com.example.avancesproyecto.ui.theme.GrisClaro
import com.example.avancesproyecto.ui.theme.VerdeOscuro
import com.example.avancesproyecto.ui.theme.White

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit = {}
) {

    var email by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var emailError by remember {
        mutableStateOf(false)
    }

    var passwordError by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        VerdeOscuro,
                        White
                    )
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
                    .size(160.dp)
                    .background(
                        color = White,
                        shape = CircleShape
                    )
                    .padding(16.dp),

                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = painterResource(
                        id = R.drawable.uam_verde
                    ),

                    contentDescription = "Logo UAM Verde",

                    modifier = Modifier.fillMaxSize(),

                    contentScale = ContentScale.Fit
                )
            }

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            Text(
                text = "UAM X Green Events",

                fontSize = 28.sp,

                color = VerdeOscuro,

                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(30.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 420.dp),

                shape = RoundedCornerShape(22.dp),

                colors = CardDefaults.cardColors(
                    containerColor = White
                ),

                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(22.dp)
                ) {

                    Text(
                        text = "Inicio de sesión",

                        fontSize = 22.sp,

                        color = VerdeOscuro,

                        fontWeight = FontWeight.Bold,

                        textAlign = TextAlign.Center,

                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(
                        modifier = Modifier.height(18.dp)
                    )

                    OutlinedTextField(
                        value = email,

                        onValueChange = {
                            email = it
                            emailError = false
                        },

                        label = {
                            Text("CIF")
                        },

                        leadingIcon = {

                            Icon(
                                Icons.Default.Email,
                                contentDescription = null,
                                tint = VerdeOscuro
                            )
                        },

                        singleLine = true,

                        isError = emailError,

                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),

                        textStyle = TextStyle(
                            color = VerdeOscuro
                        ),

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

                    if (emailError) {

                        Text(
                            text = "El CIF es obligatorio",

                            color = MaterialTheme.colorScheme.error,

                            fontSize = 12.sp
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(14.dp)
                    )

                    OutlinedTextField(
                        value = password,

                        onValueChange = {
                            password = it
                            passwordError = false
                        },

                        label = {
                            Text("Contraseña")
                        },

                        leadingIcon = {

                            Icon(
                                Icons.Default.Lock,
                                contentDescription = null,
                                tint = VerdeOscuro
                            )
                        },

                        trailingIcon = {

                            IconButton(
                                onClick = {
                                    passwordVisible =
                                        !passwordVisible
                                }
                            ) {

                                Icon(
                                    imageVector =
                                        if (passwordVisible)
                                            Icons.Default.Visibility
                                        else
                                            Icons.Default.VisibilityOff,

                                    contentDescription =
                                        "Mostrar contraseña",

                                    tint = VerdeOscuro
                                )
                            }
                        },

                        singleLine = true,

                        isError = passwordError,

                        visualTransformation =

                            if (passwordVisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),

                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),

                        textStyle = TextStyle(
                            color = VerdeOscuro
                        ),

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

                    if (passwordError) {

                        Text(
                            text = "La contraseña es obligatoria",

                            color = MaterialTheme.colorScheme.error,

                            fontSize = 12.sp
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )

                    Button(
                        onClick = {

                            emailError = email.isBlank()
                            passwordError = password.isBlank()

                            if (!emailError && !passwordError) {

                                onLoginClick()
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
                            text = "Iniciar sesión",

                            fontSize = 16.sp,

                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    TextButton(
                        onClick = { },

                        modifier = Modifier.align(
                            Alignment.CenterHorizontally
                        )
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

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {

    LoginScreen()
}