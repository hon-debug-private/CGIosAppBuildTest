package io.github.honatsugiexpress.canvasegg.resolvers.file

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import io.github.honatsugiexpress.canvasegg.data.svg.resolver.uri.SvgUriResolver
import okio.Path.Companion.toPath
import org.jetbrains.skia.Image

actual class DirectFileUriResolver actual constructor(
    actual override val baseUri: String
): SvgUriResolver {
    actual override fun resolveImage(
        uri: String,
        width: Int,
        height: Int
    ): ImageBitmap? {
        val basePath = baseUri.toPath()
        val file = basePath.resolve(uri, true).toFile()
        return Image.makeFromEncoded(file.readBytes()).toComposeImageBitmap()
    }
}