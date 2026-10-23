package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DiabloColorScheme = darkColorScheme(
    primary = DemonicGold,
    onPrimary = Color.Black,
    primaryContainer = DemonicGoldDark,
    onPrimaryContainer = DemonicGoldBright,
    secondary = BloodRed,
    onSecondary = Color.White,
    secondaryContainer = BloodRedDark,
    onSecondaryContainer = BloodRedBright,
    tertiary = ArcaneBlue,
    onTertiary = Color.White,
    tertiaryContainer = ArcaneBlueDark,
    onTertiaryContainer = ArcaneBlueBright,
    background = DiabloBackground,
    onBackground = NormalWhite,
    surface = DiabloSurface,
    onSurface = NormalWhite,
    surfaceVariant = DiabloSurfaceVariant,
    onSurfaceVariant = BoneGray,
    outline = DarkBorder,
    outlineVariant = Color(0xFF261D19),
    error = BloodRedBright,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false, // Preserve Diablo dark aesthetic
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DiabloColorScheme,
        typography = Typography,
        content = content
    )
}
