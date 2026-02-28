package io.github.honatsugiexpress.canvasegg.pdf.util.convert.transform

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathSegment.Type
import org.apache.pdfbox.pdmodel.PDPageContentStream
import kotlin.math.abs
import kotlin.math.sqrt


internal fun Path.applyContentStream(stream: PDPageContentStream) {
    var currentX = 0f
    var currentY = 0f

    var contourStartX = 0f
    var contourStartY = 0f
    iterator().forEach { segment ->
        val p = segment.points

        when (segment.type) {
            Type.Move -> {
                currentX = p[0]
                currentY = p[1]
                stream.moveTo(currentX, currentY)

                contourStartX = currentX
                contourStartY = currentY
            }

            Type.Line -> {
                currentX = p[0]
                currentY = p[1]
                stream.lineTo(currentX, currentY)
            }

            Type.Quadratic -> {
                val p0x = currentX
                val p0y = currentY
                val p1x = p[0]
                val p1y = p[1]
                val p2x = p[2]
                val p2y = p[3]

                val c1x = p0x + 2f / 3f * (p1x - p0x)
                val c1y = p0y + 2f / 3f * (p1y - p0y)

                val c2x = p2x + 2f / 3f * (p1x - p2x)
                val c2y = p2y + 2f / 3f * (p1y - p2y)

                stream.curveTo(c1x, c1y, c2x, c2y, p2x, p2y)

                currentX = p2x
                currentY = p2y
            }

            Type.Conic -> {
                val p1x = p[0]
                val p1y = p[1]
                val p2x = p[2]
                val p2y = p[3]
                val w = segment.weight

                conicToCurves(
                    stream,
                    currentX,
                    currentY,
                    p1x,
                    p1y,
                    p2x,
                    p2y,
                    w
                )

                currentX = p2x
                currentY = p2y
            }

            Type.Cubic -> {
                val endX = p[4]
                val endY = p[5]

                stream.curveTo(
                    p[0],
                    p[1],
                    p[2],
                    p[3],
                    endX,
                    endY
                )

                currentX = endX
                currentY = endY
            }

            Type.Close -> {
                stream.closePath()
                currentX = contourStartX
                currentY = contourStartY
            }

            Type.Done -> {}
        }
    }
}

private fun conicToCurves(
    contentStream: PDPageContentStream,
    p0x: Float,
    p0y: Float,
    p1x: Float,
    p1y: Float,
    p2x: Float,
    p2y: Float,
    w: Float
) {
    val epsilon = 1e-6f

    if (abs(w - 1.0f) < epsilon) {
        val c1x = p0x + 2f / 3f * (p1x - p0x)
        val c1y = p0y + 2f / 3f * (p1y - p0y)

        val c2x = p2x + 2f / 3f * (p1x - p2x)
        val c2y = p2y + 2f / 3f * (p1y - p2y)

        contentStream.curveTo(c1x, c1y, c2x, c2y, p2x, p2y)
        return
    }

    conicToCubicRecursive(contentStream, p0x, p0y, p1x, p1y, p2x, p2y, w, 0f, 1f)
}


private fun conicToCubicRecursive(
    contentStream: PDPageContentStream,
    p0x: Float,
    p0y: Float,
    p1x: Float,
    p1y: Float,
    p2x: Float,
    p2y: Float,
    w: Float,
    tMin: Float,
    tMax: Float
) {
    val sqrtW = sqrt(w)

    val tMidParam = sqrtW / (1f + sqrtW)

    val tMid = tMin + (tMax - tMin) * tMidParam

    val t = tMid
    val denom = (1f - t) * (1f - t) + 2f * w * (1f - t) * t + t * t

    val midX = (p0x * (1f - t) * (1f - t) + p1x * 2f * w * (1f - t) * t + p2x * t * t) / denom
    val midY = (p0y * (1f - t) * (1f - t) + p1y * 2f * w * (1f - t) * t + p2y * t * t) / denom

    val threshold = 1.0f
    val flatnessError = abs(p1x - midX) + abs(p1y - midY)

    if (tMax - tMin < 0.05f || flatnessError < threshold) {
        val c1x = p0x + 2f / 3f * (p1x - p0x)
        val c1y = p0y + 2f / 3f * (p1y - p0y)

        val c2x = p2x + 2f / 3f * (p1x - p2x)
        val c2y = p2y + 2f / 3f * (p1y - p2y)

        contentStream.curveTo(c1x, c1y, c2x, c2y, p2x, p2y)
        return
    }

    val invDenom = 1.0f / (w + 1f)

    val newP1X = (midX * w + p1x) * invDenom
    val newP1Y = (midY * w + p1y) * invDenom

    conicToCubicRecursive(
        contentStream,
        p0x, p0y,
        newP1X, newP1Y,
        midX, midY,
        w, tMin, tMid
    )

    conicToCubicRecursive(
        contentStream,
        midX, midY,
        newP1X, newP1Y,
        p2x, p2y,
        w, tMid, tMax
    )
}