package com.honatsugiexp.canvasegg.common.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpace
import androidx.compose.ui.graphics.colorspace.ColorSpaces

@Suppress("NOTHING_TO_INLINE")
inline fun doubleColor(
    red: Double,
    green: Double,
    blue: Double,
    alpha: Float = 1f,
    colorSpace: ColorSpace = ColorSpaces.Srgb
) = Color(
    red.toFloat(),
    green.toFloat(),
    blue.toFloat(),
    alpha,
    colorSpace
)