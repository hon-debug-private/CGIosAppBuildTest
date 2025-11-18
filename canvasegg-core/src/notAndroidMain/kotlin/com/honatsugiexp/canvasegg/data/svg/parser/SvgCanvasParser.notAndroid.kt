package com.honatsugiexp.canvasegg.data.svg.parser

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asSkiaPath

actual fun Path.hitTest(
    x: Float,
    y: Float
): Boolean {
    return asSkiaPath().contains(x, y)
}