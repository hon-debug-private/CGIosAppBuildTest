package io.github.honatsugiexpress.canvasegg.data.svg.resolver.uri

import androidx.compose.ui.graphics.ImageBitmap

object EmptyUriResolver: SvgUriResolver {
    override val baseUri: String? = null
    override fun resolveImage(uri: String, width: Int, height: Int): ImageBitmap? {
        return null
    }
}