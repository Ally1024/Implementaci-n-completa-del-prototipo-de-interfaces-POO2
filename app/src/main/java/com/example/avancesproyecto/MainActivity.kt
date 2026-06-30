package com.example.avancesproyecto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.avancesproyecto.navigation.AppNavigation
import com.example.avancesproyecto.ui.theme.AvancesProyectoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilita el diseño inmersivo adaptando la UI a los bordes de la pantalla física
        enableEdgeToEdge()

        setContent {
            // Inicialización del árbol de composición bajo el tema unificado del proyecto
            AvancesProyectoTheme {
                // Punto de control de la arquitectura de navegación de Jetpack Compose
                AppNavigation()
            }
        }
    }
}