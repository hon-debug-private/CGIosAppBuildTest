package com.honatsugiexp.canvasegg.data.svg.type.ktx

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawTransform
import androidx.compose.ui.graphics.drawscope.withTransform
import com.honatsugiexp.canvasegg.data.svg.type.SvgTransform
import com.honatsugiexp.canvasegg.data.svg.type.SvgTransformList
import kotlin.math.PI
import kotlin.math.tan

@PublishedApi
internal fun DrawTransform.applySvgTransform(transformList: SvgTransformList) {
    transformList.transforms.reversed().forEach { transform ->
        when (transform) {
            is SvgTransform.Translate -> translate(transform.x, transform.y)
            is SvgTransform.Scale -> scale(transform.x, transform.y, Offset.Zero)
            is SvgTransform.Rotate -> rotate(transform.angle, Offset(
                transform.x,
                transform.y
            ))
            is SvgTransform.SkewX -> transform(createSkewMatrix(transform.angle, true))
            is SvgTransform.SkewY -> transform(createSkewMatrix(transform.angle, false))
            is SvgTransform.Matrix -> {
                val composeMatrix = Matrix()
                composeMatrix[0, 0] = transform.a
                composeMatrix[1, 0] = transform.b
                composeMatrix[0, 1] = transform.c
                composeMatrix[1, 1] = transform.d
                composeMatrix[0, 3] = transform.e
                composeMatrix[1, 3] = transform.f
                transform(composeMatrix)
            }
        }
    }
}

@PublishedApi
internal fun Path.applySvgTransform(transformList: SvgTransformList) {
    transformList.transforms.forEach { transform ->
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
        transform(matrix)
    }
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