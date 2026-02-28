package io.github.honatsugiexpress.canvasegg.resolvers.file

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.scale
import io.github.honatsugiexpress.canvasegg.common.log.CanvasEggLogger
import io.github.honatsugiexpress.canvasegg.data.svg.resolver.uri.SvgUriResolver
import io.github.honatsugiexpress.canvasegg.data.util.BitmapSampleSizeUtil
import okio.Path.Companion.toPath

actual class DirectFileUriResolver actual constructor(
    actual override val baseUri: String
): SvgUriResolver {
    actual override fun resolveImage(
        uri: String,
        width: Int,
        height: Int
    ): ImageBitmap? {
        return try {
            val path = baseUri.toPath()
            val file = path.resolve(uri, true).toFile()
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeStream(file.inputStream(), null, options)
            options.inSampleSize =
                BitmapSampleSizeUtil.calculateInSampleSize(options, width, height)
            options.inJustDecodeBounds = false

            BitmapFactory
                .decodeStream(file.inputStream(), null, options)
                ?.scale(width, height)
                ?.asImageBitmap()
        } catch (e: Exception) {
            CanvasEggLogger.tag("DirectFileUriResolver").warnThrowable(e)
            return null
        }
    }
}