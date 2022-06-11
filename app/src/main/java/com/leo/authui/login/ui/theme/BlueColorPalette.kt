package com.leo.authui.login.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import com.leo.authui.core.ui.theme.*

val BlueDarkColorPalette = darkColors(
    surface = Blue,
    onSurface = Navy,
    primary = Navy,
    onPrimary = Chartreuse,

    /* Default colors
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
     */
)

val BlueLightColorPalette = lightColors(
    surface = Blue,
    onSurface = Color.White,
    primary = LightBlue,
    onPrimary = Navy,

    /* All the list of colors to override
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200,
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)