package com.honatsugiexp.canvasegg.data.svg.resolver

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

actual class AndroidContentUriResolver actual constructor(contentResolver: Any?) :
    SvgUriResolver {
    actual override fun resolveImage(
        uri: String,
        width: Int,
        height: Int
    ): ImageBitmap? {
        return null
    }
}

@Composable
@Suppress("ComposableNaming")
actual fun AndroidContentUriResolver(): SvgUriResolver? {
    return null
}