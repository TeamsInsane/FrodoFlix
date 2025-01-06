package com.frodo.frodoflix.ui.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color.Black,
    surface = Color(0XFFcfcfcf ),
    background = Color(0XFFf5f3ef  ),
    onPrimary = Color.White,
    onSurface = Color.Black,
    )

private val DarkColors = darkColorScheme(
    primary = Color(0xFFf5f3ef),
    surface = Color(0XFF1E1E1E),
    background = Color(0XFF121212),
    onPrimary = Color.Black,
    onSurface = Color.White,
)

@Composable
fun FrodoFlixTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content,
    )
}
