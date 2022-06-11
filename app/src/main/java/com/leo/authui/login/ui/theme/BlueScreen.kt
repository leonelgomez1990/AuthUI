package com.leo.authui.login.ui.theme

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.leo.authui.core.ui.compose.DefaultTheme

@Composable
fun BlueScreen(content: @Composable () -> Unit) {
    DefaultTheme(
        lightColorPalette = BlueLightColorPalette,
        darkColorPalette = BlueDarkColorPalette
    ) {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background,
            content = content
        )
    }
}