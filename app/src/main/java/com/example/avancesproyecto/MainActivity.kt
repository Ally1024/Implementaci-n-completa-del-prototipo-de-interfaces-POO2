package com.example.avancesproyecto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.avancesproyecto.navigation.AppNavigation
import com.example.avancesproyecto.ui.theme.AvancesProyectoTheme
import com.example.avancesproyecto.ui.theme.Screen.LoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AvancesProyectoTheme {
                AppNavigation()
            }
        }
    }
}