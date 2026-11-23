package com.yyouki.sampdroid.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SampDroidColors = darkColorScheme(
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

@Composable
fun SampDroidTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SampDroidColors,
        typography = Typography(),
        content = content,
    )
}
