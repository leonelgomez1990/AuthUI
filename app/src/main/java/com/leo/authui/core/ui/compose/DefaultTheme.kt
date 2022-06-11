package com.leo.authui.core.ui.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.leo.authui.core.ui.theme.DarkColorPalette
import com.leo.authui.core.ui.theme.LightColorPalette
import com.leo.authui.core.ui.theme.Shapes
import com.leo.authui.core.ui.theme.Typography

@Composable
fun DefaultTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    lightColorPalette: Colors = LightColorPalette,
    darkColorPalette: Colors = DarkColorPalette,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorPalette
    } else {
        lightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}