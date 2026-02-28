package io.github.honatsugiexpress.canvasegg.pdf.util.convert.transform

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.toArgb
import io.github.honatsugiexpress.canvasegg.pdf.util.convert.paint.toPDLineCap
import io.github.honatsugiexpress.canvasegg.pdf.util.convert.paint.toPDLineJoin
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.graphics.color.PDColor
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState

internal data class PDFColorInfo(
    val color: PDColor,
    val alpha: Float
)

internal fun Color.toPDFColorInfo(): PDFColorInfo {
    val argb = toArgb()

    val r = ((argb shr 16) and 0xFF) / 255f
    val g = ((argb shr 8) and 0xFF) / 255f
    val b = (argb and 0xFF) / 255f
    val a = ((argb shr 24) and 0xFF) / 255f

    return PDFColorInfo(
        color = PDColor(floatArrayOf(r, g, b), PDDeviceRGB.INSTANCE),
        alpha = a
    )
}

internal fun PDFColorInfo.setContentStream(contentStream: PDPageContentStream, paint: Paint) {
    if (paint.style == PaintingStyle.Stroke) {
        contentStream.setStrokingColor(color)
        contentStream.setLineWidth(paint.strokeWidth)
        contentStream.setLineCapStyle(paint.strokeCap.toPDLineCap())
        contentStream.setLineJoinStyle(paint.strokeJoin.toPDLineJoin())
        contentStream.setMiterLimit(paint.strokeMiterLimit)
    } else {
        contentStream.setNonStrokingColor(color)
    }
    if (alpha < 1f) {
        val graphicsState = PDExtendedGraphicsState().apply {
            nonStrokingAlphaConstant = alpha
            strokingAlphaConstant = alpha
        }
        contentStream.setGraphicsStateParameters(graphicsState)
    }
}

internal fun PDPageContentStream.fillAndStrokeContentStream(paint: Paint) {
    if (paint.style == PaintingStyle.Fill) {
        fill()
    } else if (paint.style == PaintingStyle.Stroke) {
        stroke()
    }
}