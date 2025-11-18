package com.honatsugiexp.canvasegg.data.svg.resolver

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image
import kotlin.io.encoding.Base64

actual class DataUriResolver : SvgUriResolver {
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