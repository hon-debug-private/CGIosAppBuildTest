package io.github.honatsugiexpress.canvasegg.pdf.util.convert.transform

import androidx.compose.ui.graphics.Matrix
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.transform.ClipPathTransformCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.transform.TransformCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.transform.TransformListCommand
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgTransform
import io.github.honatsugiexpress.canvasegg.pdf.util.data.cosArray
import org.apache.pdfbox.util.Matrix as PDMatrix
import org.apache.pdfbox.pdmodel.PDPageContentStream
import kotlin.math.PI
import kotlin.math.tan

internal fun TransformCommand.applyContentStream(
    contentStream: PDPageContentStream
) {
    when (val command = this) {
        is TransformListCommand -> {
            command.transformList.forEach { transform ->
                val matrix = when (transform) {
                    is SvgTransform.Translate -> Matrix().apply {
                        translate(transform.x, transform.y)
                    }
                    is SvgTransform.Scale -> Matrix().apply {
                        scale(transform.x, transform.y)
                    }
                    is SvgTransform.Rotate -> Matrix().apply {
                        translate(
                            x = -transform.x,
                            y = -transform.y
                        )
                        rotateZ(transform.angle)
                        translate(
                            x = transform.x,
                            y = transform.y
                        )
                    }

                    is SvgTransform.SkewX -> createSkewMatrix(transform.angle, true)
                    is SvgTransform.SkewY -> createSkewMatrix(transform.angle, false)
                    is SvgTransform.Matrix -> Matrix().apply {
                        set(0, 0, transform.a)
                        set(1, 0, transform.b)
                        set(0, 1, transform.c)
                        set(1, 1, transform.d)
                        set(0, 3, transform.e)
                        set(1, 3, transform.f)
                    }
                }
                contentStream.saveGraphicsState()
                contentStream.transform(matrix.toPDMatrix())
            }
        }
        is ClipPathTransformCommand -> {
            contentStream.saveGraphicsState()
            command.path.applyContentStream(contentStream)
            contentStream.clip()
        }
    }
}

private fun Matrix.toPDMatrix(): PDMatrix {
    val a = values[Matrix.ScaleX]
    val b = values[Matrix.SkewY]
    val c = values[Matrix.SkewX]
    val d = values[Matrix.ScaleY]
    val e = values[Matrix.TranslateX]
    val f = values[Matrix.TranslateY]
    return PDMatrix(a, b, c, d, e, f)
}

private fun createSkewMatrix(angleDegrees: Float, isSkewX: Boolean): Matrix {
    val skewTan = tan(toRadians(angleDegrees.toDouble())).toFloat()

    val matrix = Matrix()

    if (isSkewX) {
        matrix[0, 1] = skewTan
    } else {
        matrix[1, 0] = skewTan
    }

    return matrix
}

private fun toRadians(degrees: Double) = degrees * (PI / 180)