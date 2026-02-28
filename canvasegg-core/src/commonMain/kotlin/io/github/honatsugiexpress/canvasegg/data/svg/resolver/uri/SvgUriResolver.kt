package io.github.honatsugiexpress.canvasegg.data.svg.resolver.uri

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontFamily
import io.github.honatsugiexpress.canvasegg.data.svg.type.font.SvgFontFace

interface SvgUriResolver {
    val baseUri: String?
    fun resolveImage(uri: String, width: Int, height: Int): ImageBitmap? {
        return null
    }
    fun resolveFont(fontFace: SvgFontFace): FontFamily? {
        return null
    }
}