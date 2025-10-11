package com.example.myapplication.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = HelpMePrimaryDark,
    secondary = HelpMeSecondaryDark,
    background = HelpMeBackgroundDark,
    surface = HelpMeSurfaceDark,
    error = HelpMeError,
    onPrimary = HelpMeOnPrimary,
    onSecondary = HelpMeOnSecondary,
    onBackground = HelpMeOnBackgroundDark,
    onSurface = HelpMeOnSurfaceDark
)

private val LightColorScheme = lightColorScheme(
    primary = HelpMePrimary,
    secondary = HelpMeSecondary,
    background = HelpMeBackground,
    surface = HelpMeSurface,
    error = HelpMeError,
    onPrimary = HelpMeOnPrimary,
    onSecondary = HelpMeOnSecondary,
    onBackground = HelpMeOnBackground,
    onSurface = HelpMeOnSurface,
    primaryContainer = HelpMePrimaryContainer,
    onPrimaryContainer = HelpMeOnPrimaryContainer
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Cambiado a false para usar nuestros colores
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}