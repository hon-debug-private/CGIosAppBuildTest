package io.github.honatsugiexpress.canvasegg.data.svg.resolver.uri

import androidx.compose.ui.graphics.ImageBitmap

expect class DataUriResolver: SvgUriResolver {
    override val baseUri: String?
    override fun resolveImage(uri: String, width: Int, height: Int): ImageBitmap?
}