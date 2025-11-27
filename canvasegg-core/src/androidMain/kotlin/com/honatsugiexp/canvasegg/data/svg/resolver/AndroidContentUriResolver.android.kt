package com.honatsugiexp.canvasegg.data.svg.resolver

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.scale
import java.io.ByteArrayInputStream
import java.nio.ByteBuffer
import androidx.core.net.toUri

actual class AndroidContentUriResolver actual constructor(contentResolver: Any?): SvgUriResolver {
    val contentResolver: ContentResolver = contentResolver as ContentResolver
    actual override fun resolveImage(
        uri: String,
        width: Int,
        height: Int
    ): ImageBitmap? {
        return if (uri.trim().startsWith("content://")) {
            val contentUri = uri.toUri()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(contentResolver, contentUri)

                ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                    decoder.setTargetSize(width, height)
                    decoder.isMutableRequired = true
                }.asImageBitmap()
            } else {
                val options = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                }
                contentResolver.openInputStream(contentUri)?.use { inputStream ->
                    BitmapFactory.decodeStream(inputStream, null, options)
                    contentResolver.openInputStream(contentUri)?.use { inputStream2 ->


                        options.inSampleSize = calculateInSampleSize(options, width, height)
                        options.inJustDecodeBounds = false

                        BitmapFactory
                            .decodeStream(inputStream2, null, options)
                            ?.scale(width, height)
                            ?.asImageBitmap()
                    }
                }
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

@Composable
@Suppress("ComposableNaming")
actual fun AndroidContentUriResolver(): SvgUriResolver {
    val contentResolver = LocalContext.current.contentResolver
    return AndroidContentUriResolver(contentResolver)
}