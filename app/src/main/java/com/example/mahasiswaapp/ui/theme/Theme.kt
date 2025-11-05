package com.example.mahasiswaapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun MahasiswaAppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val lightColors = lightColorScheme(
        primary = Blue500,
        onPrimary = White,
        primaryContainer = Blue100,
        onPrimaryContainer = Black,

        secondary = Blue400,
        onSecondary = White,

        surface = White,
        onSurface = Black,

        background = White,
        onBackground = Black,

        error = ErrorRed,
        onError = White
    )

    val darkColors = darkColorScheme(
        primary = Blue200toPrimary(), // helper fallback
        onPrimary = Black,
        primaryContainer = Blue700,
        onPrimaryContainer = White,

        secondary = Blue400,
        onSecondary = Black,

        surface = Black,
        onSurface = White,

        background = Black,
        onBackground = White,

        error = ErrorRed,
        onError = Black
    )

    val colors = if (useDarkTheme) darkColors else lightColors

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}

private fun Blue200toPrimary() = Blue400
