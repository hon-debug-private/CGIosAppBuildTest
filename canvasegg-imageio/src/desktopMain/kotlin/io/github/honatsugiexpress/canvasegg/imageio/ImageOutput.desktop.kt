package io.github.honatsugiexpress.canvasegg.imageio

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toAwtImage
import okio.BufferedSink
import javax.imageio.ImageIO

internal actual fun ImageBitmap.writeTo(
    sink: BufferedSink,
    mimeType: ImageIoBase.Mimetype
) {
    val mimeTypeString = when (mimeType) {
        PngImageIO.Mimetype -> "png"
        JpegImageIO.Mimetype -> "jpeg"
        WebpImageIO.Mimetype -> "webp"
    }
    ImageIO.write(toAwtImage(), mimeTypeString, sink.outputStream())
}