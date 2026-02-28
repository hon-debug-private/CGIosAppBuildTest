package io.github.honatsugiexpress.canvasegg.data.svg.resolver.uri

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image
import kotlin.io.encoding.Base64

actual class DataUriResolver: SvgUriResolver {
    actual override val baseUri: String? = null
    actual override fun resolveImage(
        uri: String,
        width: Int,
        height: Int
    ): ImageBitmap? {
        return if (uri.contains("data:image/") && uri.contains(";base64")) {
            val data = uri.substringAfter(",")
            val byteArray = Base64.decode(data)
            Image.makeFromEncoded(byteArray).toComposeImageBitmap()
        } else null
    }
}