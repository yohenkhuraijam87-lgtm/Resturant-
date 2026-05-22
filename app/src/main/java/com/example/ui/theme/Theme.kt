package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = VelouraGold,
    secondary = VelouraOrange,
    tertiary = VelouraChampagne,
    background = VelouraBlack,
    surface = VelouraCharcoal,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onTertiary = Color.Black,
    onBackground = TextWhite,
    onSurface = TextWhite,
    error = SoftCrimson,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force Dark Luxury Mode by default
    content: @Composable () -> Unit
) {
    // We intentionally force our custom curated Dark Luxury Colors to bypass standard system tints,
    // preserving the brand's premium immersive night-life mood.
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
