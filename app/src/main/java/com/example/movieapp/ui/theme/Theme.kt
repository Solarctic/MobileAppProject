package com.example.movieapp.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.movieapp.R

// 自定义奢华字体
val LuxuryFont = FontFamily(
    Font(R.font.playfair_display_regular, FontWeight.Normal)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFE50914),      // Netflix红
    onPrimary = Color.White,
    background = Color.Black,
    onBackground = Color.White,
    surface = Color(0xFF1A1A1A),
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFE50914),
    onPrimary = Color.White,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color(0xFFF5F5F5),
    onSurface = Color.Black
)

val AppTypography = Typography(
    headlineLarge = androidx.compose.ui.text.TextStyle(
        fontFamily = LuxuryFont,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        color = Color.White
    ),
    titleLarge = androidx.compose.ui.text.TextStyle(
        fontFamily = LuxuryFont,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        color = Color.White
    ),
    bodyMedium = androidx.compose.ui.text.TextStyle(
        fontSize = 16.sp,
        color = Color.White
    )
)

@Composable
fun MoviesAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme, // 强制黑红风格
        typography = AppTypography,
        content = content
    )
}
