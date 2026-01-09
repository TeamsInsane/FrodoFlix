package com.frodo.frodoflix.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Modern Color Palette
private val ModernBlue = Color(0xFF2196F3)
private val ModernPurple = Color(0xFF9C27B0)
private val ModernTeal = Color(0xFF00BCD4)

// Light Theme Colors
private val LightColors = lightColorScheme(
    primary = Color(0xFF1976D2),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBBDEFB),
    onPrimaryContainer = Color(0xFF0D47A1),

    secondary = Color(0xFF7E57C2),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE1BEE7),
    onSecondaryContainer = Color(0xFF4A148C),

    tertiary = Color(0xFF26C6DA),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFB2EBF2),
    onTertiaryContainer = Color(0xFF006064),

    background = Color(0xFFF5F5F7),
    onBackground = Color(0xFF1C1B1F),

    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454F),

    error = Color(0xFFB00020),
    onError = Color.White
)

// Dark Theme Colors - Modern Netflix-style
private val DarkColors = darkColorScheme(
    primary = Color(0xFF82B1FF),
    onPrimary = Color(0xFF003366),
    primaryContainer = Color(0xFF1976D2),
    onPrimaryContainer = Color(0xFFBBDEFB),

    secondary = Color(0xFFCE93D8),
    onSecondary = Color(0xFF3E2723),
    secondaryContainer = Color(0xFF7E57C2),
    onSecondaryContainer = Color(0xFFE1BEE7),

    tertiary = Color(0xFF80DEEA),
    onTertiary = Color(0xFF003333),
    tertiaryContainer = Color(0xFF26C6DA),
    onTertiaryContainer = Color(0xFFB2EBF2),

    background = Color(0xFF0F0F0F),
    onBackground = Color(0xFFE6E1E5),

    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF2B2930),
    onSurfaceVariant = Color(0xFFCAC4D0),

    error = Color(0xFFCF6679),
    onError = Color(0xFF690005)
)

@Composable
fun FrodoFlixTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}