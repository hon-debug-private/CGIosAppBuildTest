package com.honatsugiexp.canvasegg.imageio

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import okio.BufferedSink
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
internal actual fun ImageBitmap.writeTo(
    sink: BufferedSink,
    mimeType: ImageIoBase.Mimetype
) {
    val bitmap = asSkiaBitmap()
    val format = when (mimeType) {
        PngImageIO.Mimetype -> EncodedImageFormat.PNG
        JpegImageIO.Mimetype -> EncodedImageFormat.JPEG
        WebpImageIO.Mimetype -> EncodedImageFormat.WEBP
    }
    val data = Image.makeFromBitmap(bitmap).encodeToData(format) ?: error("Image encoding failed")
    val bytes = data.getBytes(0, data.size)
    sink.write(bytes)
}