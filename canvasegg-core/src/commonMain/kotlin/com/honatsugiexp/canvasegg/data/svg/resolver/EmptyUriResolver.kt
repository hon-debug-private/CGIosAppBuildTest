package com.honatsugiexp.canvasegg.data.svg.resolver

import androidx.compose.ui.graphics.ImageBitmap

object EmptyUriResolver: SvgUriResolver {
    override fun resolveImage(uri: String, width: Int, height: Int): ImageBitmap? {
        return null
    }
}