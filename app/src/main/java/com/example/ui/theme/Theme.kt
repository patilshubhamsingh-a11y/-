package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DevotionalLightColorScheme = lightColorScheme(
    primary = SaffronPrimary,
    onPrimary = Color.White,
    primaryContainer = GoldContainer,
    onPrimaryContainer = SaffronDark,
    secondary = TempleMaroon,
    onSecondary = Color.White,
    secondaryContainer = TempleMaroonContainer,
    onSecondaryContainer = TempleMaroon,
    tertiary = GoldAccent,
    onTertiary = Color(0xFF3E2723),
    tertiaryContainer = GoldLight,
    onTertiaryContainer = Color(0xFF4E342E),
    background = DevotionalBgLight,
    onBackground = Color(0xFF2C241E),
    surface = DevotionalSurfaceLight,
    onSurface = Color(0xFF2C241E),
    surfaceVariant = DevotionalSurfaceVariantLight,
    onSurfaceVariant = Color(0xFF5D4037),
    outline = DevotionalOutlineLight
)

private val DevotionalDarkColorScheme = darkColorScheme(
    primary = SaffronDarkTheme,
    onPrimary = Color(0xFF431600),
    primaryContainer = SaffronContainerDark,
    onPrimaryContainer = GoldLight,
    secondary = Color(0xFFF48FB1),
    onSecondary = Color(0xFF560027),
    secondaryContainer = Color(0xFF880E4F),
    onSecondaryContainer = Color(0xFFFFD8E4),
    tertiary = GoldAccent,
    onTertiary = Color(0xFF3E2723),
    tertiaryContainer = Color(0xFF5D4037),
    onTertiaryContainer = GoldLight,
    background = DevotionalBgDark,
    onBackground = Color(0xFFEDE0D4),
    surface = DevotionalSurfaceDark,
    onSurface = Color(0xFFEDE0D4),
    surfaceVariant = DevotionalSurfaceVariantDark,
    onSurfaceVariant = Color(0xFFD7CCC8),
    outline = Color(0xFF5D4037)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Preserve devotional theme identity
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DevotionalDarkColorScheme
        else -> DevotionalLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
