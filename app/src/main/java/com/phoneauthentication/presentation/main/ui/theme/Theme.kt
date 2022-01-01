package com.phoneauthentication.presentation.main.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color.DarkGray,
    primaryVariant = Color.DarkGray,
    secondary = Mauve
)

private val LightColorPalette = lightColors(
    primary = ColumbiaBlue,
    primaryVariant = ColumbiaBlue,
    secondary = CarnationPink

)

@Composable
fun PhoneAuthenticationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}