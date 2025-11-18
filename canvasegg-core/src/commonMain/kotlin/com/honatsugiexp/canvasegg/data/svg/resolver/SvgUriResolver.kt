package com.honatsugiexp.canvasegg.data.svg.resolver

import androidx.compose.ui.graphics.ImageBitmap

interface SvgUriResolver {
    fun resolveImage(uri: String, width: Int, height: Int): ImageBitmap?
}