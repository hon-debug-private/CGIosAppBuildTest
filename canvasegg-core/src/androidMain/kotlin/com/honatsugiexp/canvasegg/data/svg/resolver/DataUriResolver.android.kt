package com.honatsugiexp.canvasegg.data.svg.resolver

import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.scale
import java.io.ByteArrayInputStream
import java.nio.ByteBuffer
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

                options.inSampleSize = calculateInSampleSize(options, width, height)
                options.inJustDecodeBounds = false

                BitmapFactory
                    .decodeStream(ByteArrayInputStream(byteArray), null, options)
                    ?.scale(width, height)
                    ?.asImageBitmap()
            }
        } else null
    }
    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}