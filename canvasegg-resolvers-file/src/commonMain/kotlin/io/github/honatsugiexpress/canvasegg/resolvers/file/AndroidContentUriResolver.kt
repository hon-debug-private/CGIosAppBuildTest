package io.github.honatsugiexpress.canvasegg.resolvers.file

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontFamily
import io.github.honatsugiexpress.canvasegg.data.svg.resolver.uri.SvgUriResolver
import io.github.honatsugiexpress.canvasegg.data.svg.type.font.SvgFontFace
import okio.BufferedSource

expect class AndroidContentUriResolver(
    context: Any?,
    baseUri: String,
    fontCopyFunction: FontCopyFunction
): SvgUriResolver {
    override val baseUri: String
    override fun resolveImage(uri: String, width: Int, height: Int): ImageBitmap?
    override fun resolveFont(fontFace: SvgFontFace): FontFamily?
}

fun interface FontCopyFunction {
    fun onRequest(request: FontCopyRequest): String?
}

data class FontCopyRequest(
    val source: BufferedSource,
    val path: String
)

@Composable
@Suppress("ComposableNaming")
expect fun AndroidContentUriResolver(baseUri: String, fontCopyFunction: FontCopyFunction): SvgUriResolver