package io.github.honatsugiexpress.canvasegg.resolvers.file

import androidx.compose.ui.graphics.ImageBitmap
import io.github.honatsugiexpress.canvasegg.data.svg.resolver.uri.SvgUriResolver

actual class DirectFileUriResolver actual constructor(
    actual override val baseUri: String
): SvgUriResolver {
    actual override fun resolveImage(
        uri: String,
        width: Int,
        height: Int
    ): ImageBitmap? {
        return null
    }
}