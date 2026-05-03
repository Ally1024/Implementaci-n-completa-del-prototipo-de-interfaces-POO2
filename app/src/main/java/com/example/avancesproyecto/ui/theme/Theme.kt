package com.example.avancesproyecto.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = VerdeOscuro,
    secondary = GrisClaro,
    tertiary = VerdeOscuro,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = White,
    onSecondary = Color.Black,
    onTertiary = White,
    onBackground = White,
    onSurface = White
)

private val LightColorScheme = lightColorScheme(
    primary = VerdeOscuro,
    secondary = GrisClaro,
    tertiary = VerdeOscuro,
    background = White,
    surface = GrisClaro,
    onPrimary = White,
    onSecondary = Color.Black,
    onTertiary = White,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun AvancesProyectoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}