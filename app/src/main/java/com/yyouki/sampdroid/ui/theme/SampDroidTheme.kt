package com.yyouki.sampdroid.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SampDroidDarkColors = darkColorScheme(
    primary = Color(0xFFB7A1FF),
    onPrimary = Color(0xFF21134C),
    secondary = Color(0xFF80E0BD),
    background = Color(0xFF090B10),
    surface = Color(0xFF141923),
    surfaceVariant = Color(0xFF202938),
    onBackground = Color(0xFFE8EAF1),
    onSurface = Color(0xFFE8EAF1),
    onSurfaceVariant = Color(0xFFB5BECC),
)

private val SampDroidLightColors = lightColorScheme(
    primary = Color(0xFF5A43AA),
    onPrimary = Color.White,
    secondary = Color(0xFF176B50),
    onSecondary = Color.White,
    background = Color(0xFFF4F5F9),
    surface = Color.White,
    surfaceVariant = Color(0xFFE4E8F0),
    onBackground = Color(0xFF171B26),
    onSurface = Color(0xFF171B26),
    onSurfaceVariant = Color(0xFF596273),
)

@Composable
fun SampDroidTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) SampDroidDarkColors else SampDroidLightColors,
        typography = Typography(),
        content = content,
    )
}
