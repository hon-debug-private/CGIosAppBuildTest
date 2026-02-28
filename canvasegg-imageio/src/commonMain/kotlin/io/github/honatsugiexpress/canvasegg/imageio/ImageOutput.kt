package io.github.honatsugiexpress.canvasegg.imageio

import androidx.compose.ui.graphics.ImageBitmap
import okio.BufferedSink

internal expect fun ImageBitmap.writeTo(sink: BufferedSink, mimeType: ImageIoBase.Mimetype)