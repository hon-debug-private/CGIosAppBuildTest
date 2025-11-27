package com.honatsugiexp.canvasegg.data.svg.resolver

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

expect class AndroidContentUriResolver(contentResolver: Any?): SvgUriResolver {
    override fun resolveImage(uri: String, width: Int, height: Int): ImageBitmap?
}

@Composable
@Suppress("ComposableNaming")
expect fun AndroidContentUriResolver(): SvgUriResolver