package io.github.honatsugiexpress.canvasegg.data.svg.resolver.uri

import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.scale
import io.github.honatsugiexpress.canvasegg.data.util.BitmapSampleSizeUtil
import java.io.ByteArrayInputStream
import java.nio.ByteBuffer
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val source = ImageDecoder.createSource(byteArray)

                    ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                        decoder.setTargetSize(width, height)
                        decoder.isMutableRequired = true
                    }.asImageBitmap()
                } else {
                    val buffer = ByteBuffer.wrap(byteArray)
                    val source = ImageDecoder.createSource(buffer)

                    ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                        decoder.setTargetSize(width, height)
                        decoder.isMutableRequired = true
                    }.asImageBitmap()
                }
            } else {
                val options = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                }
                BitmapFactory.decodeStream(ByteArrayInputStream(byteArray), null, options)

                options.inSampleSize = BitmapSampleSizeUtil.calculateInSampleSize(options, width, height)
                options.inJustDecodeBounds = false

                BitmapFactory
                    .decodeStream(ByteArrayInputStream(byteArray), null, options)
                    ?.scale(width, height)
                    ?.asImageBitmap()
            }
        } else null
    }
}