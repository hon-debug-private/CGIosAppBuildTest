package io.github.honatsugiexpress.canvasegg.resolvers.file

import androidx.compose.ui.graphics.ImageBitmap
import io.github.honatsugiexpress.canvasegg.data.svg.resolver.uri.SvgUriResolver

expect class IosUriResolver: SvgUriResolver {
    override val baseUri: String
    override fun resolveImage(uri: String, width: Int, height: Int): ImageBitmap?
}