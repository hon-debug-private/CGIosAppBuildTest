package io.github.honatsugiexpress.canvasegg.imageio

import android.graphics.Bitmap.CompressFormat
import android.os.Build
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import okio.BufferedSink

internal actual fun ImageBitmap.writeTo(
    sink: BufferedSink,
    mimeType: ImageIoBase.Mimetype
) {
    val type = when (mimeType) {
        PngImageIO.Mimetype -> CompressFormat.PNG
        JpegImageIO.Mimetype -> CompressFormat.JPEG
        WebpImageIO.Mimetype -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            CompressFormat.WEBP_LOSSLESS
        } else {
            CompressFormat.WEBP
        }
    }
    asAndroidBitmap().compress(
        type,
        100,
        sink.outputStream()
    )
}