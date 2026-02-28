package io.github.honatsugiexpress.canvasegg.pdf.util

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject

internal fun PDPageContentStream.drawCanvas(
    document: PDDocument,
    rect: Rect,
    block: Canvas.() -> Unit
) {
    val widthInt = rect.width.toInt()
    val heightInt = rect.height.toInt()
    val bitmap = ImageBitmap(widthInt, heightInt)
    val canvas = Canvas(bitmap)
    CanvasDrawScope().draw(
        Density(1f),
        LayoutDirection.Ltr,
        canvas,
        Size(rect.width, rect.height)
    ) {
        drawIntoCanvas {
            it.block()
        }
    }
    val buffer = IntArray(widthInt * heightInt)
    bitmap.readPixels(
        buffer = buffer,
        startX = 0,
        startY = 0,
        width = widthInt,
        height = heightInt
    )
    val byteArray = ByteArray(buffer.size * 4)

    for (i in buffer.indices) {
        val pixel = buffer[i]

        val a = (pixel shr 24) and 0xFF
        val r = (pixel shr 16) and 0xFF
        val g = (pixel shr 8) and 0xFF
        val b = pixel and 0xFF

        byteArray[i * 4 + 0] = r.toByte()
        byteArray[i * 4 + 1] = g.toByte()
        byteArray[i * 4 + 2] = b.toByte()
        byteArray[i * 4 + 3] = a.toByte()
    }
    val image = PDImageXObject.createFromByteArray(document, byteArray, null)
    drawImage(image, rect.left, rect.top, rect.width, rect.height)
}