package com.honatsugiexp.canvasegg.data.svg.resolver

import androidx.compose.ui.graphics.ImageBitmap

expect class DataUriResolver: SvgUriResolver {
    override fun resolveImage(uri: String, width: Int, height: Int): ImageBitmap?
}