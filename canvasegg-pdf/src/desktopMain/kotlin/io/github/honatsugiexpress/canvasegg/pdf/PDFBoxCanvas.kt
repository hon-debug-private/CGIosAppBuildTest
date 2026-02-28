package io.github.honatsugiexpress.canvasegg.pdf

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.Vertices
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import io.github.honatsugiexpress.canvasegg.pdf.data.PdfLayer
import io.github.honatsugiexpress.canvasegg.pdf.util.convert.transform.applyContentStream
import io.github.honatsugiexpress.canvasegg.pdf.util.convert.transform.fillAndStrokeContentStream
import io.github.honatsugiexpress.canvasegg.pdf.util.convert.transform.setContentStream
import io.github.honatsugiexpress.canvasegg.pdf.util.convert.transform.toPDFColorInfo
import io.github.honatsugiexpress.canvasegg.pdf.util.drawCanvas
import org.apache.pdfbox.cos.COSName
import org.apache.pdfbox.cos.COSStream
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject
import org.apache.pdfbox.pdmodel.graphics.form.PDTransparencyGroupAttributes
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.tan
import org.apache.pdfbox.util.Matrix as PDMatrix

class PDFBoxCanvas(
    val document: PDDocument,
    val page: PDPage
): Canvas {
    private var contentStream: PDPageContentStream = PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)
    private val layerStack = ArrayDeque<PdfLayer>()
    override fun save() {
        contentStream.saveGraphicsState()
    }

    override fun restore() {
        if (layerStack.isEmpty()) {
            contentStream.restoreGraphicsState()
            return
        }

        contentStream.close()

        val layer = layerStack.removeLast()

        contentStream = layer.contentStream

        val alpha = layer.paint.alpha
        if (alpha < 1f) {
            val gs = PDExtendedGraphicsState().apply {
                strokingAlphaConstant = alpha
                nonStrokingAlphaConstant = alpha
                alphaSourceFlag = true
            }
            contentStream.setGraphicsStateParameters(gs)
        }

        contentStream.drawForm(layer.form)
    }

    override fun saveLayer(
        bounds: Rect,
        paint: Paint
    ) {
        val form = PDFormXObject(document)

        form.bBox = PDRectangle(
            bounds.left,
            bounds.top,
            bounds.width,
            bounds.height
        )

        val group = PDTransparencyGroupAttributes().apply {
            cosObject.setBoolean(COSName.I, true)
        }
        form.group = group

        val formStream = PDPageContentStream(
            document,
            PDAppearanceStream(form.cosObject as COSStream)
        )

        layerStack.addLast(
            PdfLayer(
                form = form,
                contentStream = contentStream,
                paint = paint
            )
        )

        contentStream = formStream
    }

    override fun translate(dx: Float, dy: Float) {
        contentStream.transform(PDMatrix.getTranslateInstance(dx, dy))
    }

    override fun scale(sx: Float, sy: Float) {
        contentStream.transform(PDMatrix.getScaleInstance(sx, sy))
    }

    override fun rotate(degrees: Float) {
        contentStream.transform(PDMatrix.getRotateInstance(degrees * 180f / PI, 0f, 0f))
    }

    override fun skew(sx: Float, sy: Float) {
        contentStream.transform(PDMatrix(1f, sy, sx, 1f, 0f, 0f))
    }

    override fun concat(matrix: Matrix) {
        val a = matrix.values[Matrix.ScaleX]
        val b = matrix.values[Matrix.SkewY]
        val c = matrix.values[Matrix.SkewX]
        val d = matrix.values[Matrix.ScaleY]
        val e = matrix.values[Matrix.TranslateX]
        val f = matrix.values[Matrix.TranslateY]
        contentStream.transform(PDMatrix(a, b, c, d, e, f))
    }

    override fun clipRect(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        clipOp: ClipOp
    ) {
        contentStream.saveGraphicsState()
        if (clipOp == ClipOp.Intersect) {
            contentStream.addRect(left, top, right - left, bottom - top)
        } else if (clipOp == ClipOp.Difference) {
            val mediaBox = page.mediaBox
            contentStream.addRect(mediaBox.lowerLeftX, mediaBox.lowerLeftY, mediaBox.width, mediaBox.height)

            contentStream.moveTo(left, top)
            contentStream.lineTo(left, bottom)
            contentStream.lineTo(right, bottom)
            contentStream.lineTo(right, top)
            contentStream.closePath()
        }
        contentStream.clip()
    }

    override fun clipPath(
        path: Path,
        clipOp: ClipOp
    ) {
        contentStream.saveGraphicsState()

        if (clipOp == ClipOp.Intersect) {
            path.applyContentStream(contentStream)
            contentStream.clip()
        } else if (clipOp == ClipOp.Difference) {
            val mediaBox = page.mediaBox
            contentStream.addRect(
                mediaBox.lowerLeftX,
                mediaBox.lowerLeftY,
                mediaBox.width,
                mediaBox.height
            )

            path.applyContentStream(contentStream)

            contentStream.clipEvenOdd()
        }
    }

    override fun drawLine(
        p1: Offset,
        p2: Offset,
        paint: Paint
    ) {
        if (paint.shader != null) {
            contentStream.drawCanvas(document, Rect(p1, p2)) {
                drawLine(p1, p2, paint)
            }
        } else {
            val info = paint.color.toPDFColorInfo()
            info.setContentStream(contentStream, paint)
            contentStream.moveTo(p1.x, p1.y)
            contentStream.lineTo(p2.x, p2.y)
            contentStream.fillAndStrokeContentStream(paint)
        }
    }

    override fun drawRect(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        paint: Paint
    ) {
        val rect = Rect(left, top, right, bottom)
        if (paint.shader != null) {
            contentStream.drawCanvas(document, rect) {
                drawRect(left, top, right, bottom, paint)
            }
        } else {
            val info = paint.color.toPDFColorInfo()
            info.setContentStream(contentStream, paint)
            contentStream.addRect(rect.left, rect.top, rect.width, rect.height)
            contentStream.fillAndStrokeContentStream(paint)
        }
    }

    override fun drawRoundRect(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        radiusX: Float,
        radiusY: Float,
        paint: Paint
    ) {
        val width = right - left
        val height = bottom - top
        if (paint.shader != null) {
            contentStream.drawCanvas(document, Rect(left, top, right, bottom)) {
                drawRoundRect(left, top, right, bottom, radiusX, radiusY, paint)
            }
        } else {
            val info = paint.color.toPDFColorInfo()
            info.setContentStream(contentStream, paint)
            contentStream.moveTo(left + radiusX, radiusY)
            contentStream.lineTo(left + width - radiusX, top)
            contentStream.curveTo(left + width, top, left + width, top + radiusY, left + width, top + radiusY)
            contentStream.lineTo(left + width, top + height - radiusY)
            contentStream.curveTo(
                left + width,
                top + height,
                left + width - radiusX,
                top + height,
                left + width - radiusX,
                top + height
            )
            contentStream.lineTo(left + radiusX, top + height)
            contentStream.curveTo(left, top + height, left, top + height - radiusY, left, top + height - radiusY)
            contentStream.lineTo(left, top + radiusY)
            contentStream.curveTo(left, top, left + radiusX, top, left + radiusX, top)
            contentStream.closePath()
            contentStream.fillAndStrokeContentStream(paint)
        }
    }

    override fun drawOval(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        paint: Paint
    ) {
        val width = right - left
        val height = bottom - top
        val radiusX = width / 2.0f
        val radiusY = height / 2.0f
        val centerX = left + radiusX
        val centerY = top + radiusY

        val k = 0.552284749831
        val xOffset = (radiusX * k).toFloat()
        val yOffset = (radiusY * k).toFloat()

        val info = paint.color.toPDFColorInfo()
        info.setContentStream(contentStream, paint)

        contentStream.moveTo(centerX + radiusX, centerY)
        contentStream.curveTo(centerX + radiusX, centerY + yOffset,
            centerX + xOffset, centerY + radiusY,
            centerX, centerY + radiusY)
        contentStream.curveTo(centerX - xOffset, centerY + radiusY,
            centerX - radiusX, centerY + yOffset,
            centerX - radiusX, centerY)
        contentStream.curveTo(centerX - radiusX, centerY - yOffset,
            centerX - xOffset, centerY - radiusY,
            centerX, centerY - radiusY)
        contentStream.curveTo(centerX + xOffset, centerY - radiusY,
            centerX + radiusX, centerY - yOffset,
            centerX + radiusX, centerY)

        contentStream.closePath()

        contentStream.fillAndStrokeContentStream(paint)
    }

    override fun drawCircle(
        center: Offset,
        radius: Float,
        paint: Paint
    ) {
        drawOval(center.x - radius, center.y - radius, center.x + radius, center.y + radius, paint)
    }

    override fun drawArc(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        startAngle: Float,
        sweepAngle: Float,
        useCenter: Boolean,
        paint: Paint
    ) {
        val width = right - left
        val height = bottom - top
        val rx = width / 2f
        val ry = height / 2f
        val centerX = left + rx
        val centerY = top + ry

        val info = paint.color.toPDFColorInfo()
        info.setContentStream(contentStream, paint)

        val segments = ceil(abs(sweepAngle) / 90.0).toInt()
        val anglePerSegment = sweepAngle / segments

        var currentAngle = startAngle

        val startRad = Math.toRadians(currentAngle.toDouble())
        val startX = centerX + rx * Math.cos(startRad).toFloat()
        val startY = centerY + ry * Math.sin(startRad).toFloat()

        if (useCenter) {
            contentStream.moveTo(centerX, centerY)
            contentStream.lineTo(startX, startY)
        } else {
            contentStream.moveTo(startX, startY)
        }

        for (i in 0 until segments) {
            val nextAngle = currentAngle + anglePerSegment

            val a1 = Math.toRadians(currentAngle.toDouble())
            val a2 = Math.toRadians(nextAngle.toDouble())

            val factor = tan((a2 - a1) / 4.0)
            val k = (4.0 / 3.0 * factor).toFloat()

            val endX = centerX + rx * Math.cos(a2).toFloat()
            val endY = centerY + ry * Math.sin(a2).toFloat()

            val cp1x = centerX + rx * (Math.cos(a1) - k * Math.sin(a1)).toFloat()
            val cp1y = centerY + ry * (Math.sin(a1) + k * Math.cos(a1)).toFloat()

            val cp2x = centerX + rx * (Math.cos(a2) + k * Math.sin(a2)).toFloat()
            val cp2y = centerY + ry * (Math.sin(a2) - k * Math.cos(a2)).toFloat()

            contentStream.curveTo(cp1x, cp1y, cp2x, cp2y, endX, endY)

            currentAngle = nextAngle
        }

        if (useCenter) {
            contentStream.closePath()
        }

        contentStream.fillAndStrokeContentStream(paint)
    }

    override fun drawPath(
        path: Path,
        paint: Paint
    ) {
        path.applyContentStream(contentStream)
        contentStream.fillAndStrokeContentStream(paint)
    }

    override fun drawImage(
        image: ImageBitmap,
        topLeftOffset: Offset,
        paint: Paint
    ) {

    }

    override fun drawImageRect(
        image: ImageBitmap,
        srcOffset: IntOffset,
        srcSize: IntSize,
        dstOffset: IntOffset,
        dstSize: IntSize,
        paint: Paint
    ) {

    }

    override fun drawPoints(
        pointMode: PointMode,
        points: List<Offset>,
        paint: Paint
    ) {
        TODO("Not yet implemented")
    }

    override fun drawRawPoints(
        pointMode: PointMode,
        points: FloatArray,
        paint: Paint
    ) {
        TODO("Not yet implemented")
    }

    override fun drawVertices(
        vertices: Vertices,
        blendMode: BlendMode,
        paint: Paint
    ) {
        TODO("Not yet implemented")
    }

    override fun enableZ() {
        TODO("Not yet implemented")
    }

    override fun disableZ() {
        TODO("Not yet implemented")
    }
}