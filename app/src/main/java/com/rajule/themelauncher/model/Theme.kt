package com.rajule.themelauncher.model

import androidx.compose.ui.graphics.Color

// A theme is pure data: palette + wallpaper filename + per-page tile layout.
data class ThemePalette(
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val onSurface: Color,
    val onSurfaceMuted: Color,
    val accent: Color,
    val gradientTop: Color,
    val gradientBottom: Color
)

data class ThemeDefinition(
    val id: String,
    val displayName: String,
    val palette: ThemePalette,
    val pages: List<List<GridItem>>,
    val dock: List<AppRole>
)
