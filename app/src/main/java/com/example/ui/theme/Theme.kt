package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = BrandRed,
  onPrimary = Color.White,
  primaryContainer = BrandRedDark,
  onPrimaryContainer = Color.White,
  secondary = BrandPrimeBlue,
  onSecondary = Color.White,
  secondaryContainer = StreamDarkElevated,
  onSecondaryContainer = Color.White,
  tertiary = AccentAmber,
  onTertiary = Color.Black,
  background = StreamDarkBackground,
  onBackground = StreamTextPrimary,
  surface = StreamDarkSurface,
  onSurface = StreamTextPrimary,
  surfaceVariant = StreamDarkSurfaceVariant,
  onSurfaceVariant = StreamTextSecondary,
  outline = StreamDarkElevated
)

@Composable
fun MyApplicationTheme(
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = DarkColorScheme,
    typography = Typography,
    content = content
  )
}
