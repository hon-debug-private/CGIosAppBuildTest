package io.github.honatsugiexpress.canvasegg.resolvers.file

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Typeface as ComposeTypeface
import io.github.honatsugiexpress.canvasegg.common.log.CanvasEggLogger
import io.github.honatsugiexpress.canvasegg.data.svg.resolver.uri.SvgUriResolver
import io.github.honatsugiexpress.canvasegg.data.svg.type.font.SvgFontFace
import io.github.honatsugiexpress.canvasegg.data.svg.type.font.SvgFontFaceSrcKeyword
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import okio.Path.Companion.toPath
import org.jetbrains.skia.Data
import org.jetbrains.skia.FontMgr
import org.jetbrains.skia.Image
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
actual class IosUriResolver(
    actual override val baseUri: String
): SvgUriResolver {
    actual override fun resolveImage(
        uri: String,
        width: Int,
        height: Int
    ): ImageBitmap? {
        val basePath = baseUri.toPath()
        val absoluteUri = basePath.resolve(uri, true).toString()
        val nsUri = NSURL.fileURLWithPath(absoluteUri)
        val data = NSData.dataWithContentsOfURL(nsUri) ?: return null
        val bytes = ByteArray(data.length.toInt()).apply {
            usePinned {
                memcpy(it.addressOf(0), data.bytes, data.length)
            }
        }
        return Image.makeFromEncoded(bytes).toComposeImageBitmap()
    }

    override fun resolveFont(fontFace: SvgFontFace): FontFamily? {
        val basePath = baseUri.toPath()
        var fontFamily: FontFamily? = null
        fontFace.src.forEach { keyword ->
            try {
                val font: FontFamily = when (keyword) {
                    is SvgFontFaceSrcKeyword.Url -> {
                        val absoluteUri = basePath.resolve(keyword.value, true).toString()
                        val nsUri = NSURL.fileURLWithPath(absoluteUri)
                        val nsData = NSData.dataWithContentsOfURL(nsUri) ?: return@forEach
                        val bytes = ByteArray(nsData.length.toInt()).apply {
                            usePinned {
                                memcpy(it.addressOf(0), nsData.bytes, nsData.length)
                            }
                        }
                        val data = Data.makeFromBytes(bytes)
                        val skiaTypeface = FontMgr.default.makeFromData(data) ?: return@forEach
                        val typeface = ComposeTypeface(skiaTypeface)
                        FontFamily(typeface)
                    }
                    else -> return@forEach
                }
                fontFamily = font
            } catch (e: Exception) {
                CanvasEggLogger.warnThrowable(e)
            }
        }
        return fontFamily
    }
}