package io.github.honatsugiexpress.canvasegg.data.svg.type.util

import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgTransform

internal fun SvgTransform.name() = when (this) {
    is SvgTransform.Translate -> SvgTransform.TRANSLATE_NAME
    is SvgTransform.Scale -> SvgTransform.SCALE_NAME
    is SvgTransform.Rotate -> SvgTransform.ROTATE_NAME
    is SvgTransform.SkewX -> SvgTransform.SKEW_X_NAME
    is SvgTransform.SkewY -> SvgTransform.SKEW_Y_NAME
    is SvgTransform.Matrix -> SvgTransform.MATRIX_NAME
}

internal fun SvgTransform.size() = when (this) {
    is SvgTransform.Translate -> 2
    is SvgTransform.Scale -> 2
    is SvgTransform.Rotate -> 3
    is SvgTransform.SkewX -> 1
    is SvgTransform.SkewY -> 1
    is SvgTransform.Matrix -> 6
}

internal fun SvgTransform.type() = when (this) {
    is SvgTransform.Translate -> SvgTransform.TRANSLATE_ID
    is SvgTransform.Scale -> SvgTransform.SCALE_ID
    is SvgTransform.Rotate -> SvgTransform.ROTATE_ID
    is SvgTransform.SkewX -> SvgTransform.SKEW_X_ID
    is SvgTransform.SkewY -> SvgTransform.SKEW_Y_ID
    is SvgTransform.Matrix -> SvgTransform.MATRIX_ID
}

internal fun SvgTransform.writeData(array: FloatArray, offset: Int) = when (this) {
    is SvgTransform.Translate -> {
        array[offset] = x
        array[offset + 1] = y
        offset + 2
    }
    is SvgTransform.Scale -> {
        array[offset] = x
        array[offset + 1] = y
        offset + 2
    }
    is SvgTransform.Rotate -> {
        array[offset] = angle
        array[offset + 1] = x
        array[offset + 2] = y
        offset + 3
    }
    is SvgTransform.SkewX -> {
        array[offset] = angle
        offset + 1
    }
    is SvgTransform.SkewY -> {
        array[offset] = angle
        offset + 1
    }
    is SvgTransform.Matrix -> {
        array[offset] = a
        array[offset + 1] = b
        array[offset + 2] = c
        array[offset + 3] = d
        array[offset + 4] = e
        array[offset + 5] = f
        offset + 6
    }
}

@PublishedApi
internal fun SvgTransform.Companion.fromArray(array: FloatArray, offset: Int): Pair<Int, SvgTransform> {
    val type = array[offset].toInt()
    return when (type) {
        0 -> {
            val x = array[offset + 1]
            val y = array[offset + 2]
            offset + 3 to SvgTransform.Translate(x, y)
        }
        1 -> {
            val x = array[offset + 1]
            val y = array[offset + 2]
            offset + 3 to SvgTransform.Scale(x, y)
        }
        2 -> {
            offset + 5 to SvgTransform.Rotate(array.copyOfRange(offset + 1, offset + 4))
        }
        3 -> {
            val angle = array[offset + 1]
            offset + 2 to SvgTransform.SkewX(angle)
        }
        4 -> {
            val angle = array[offset + 1]
            offset + 2 to SvgTransform.SkewY(angle)
        }
        5 -> {
            offset + 8 to SvgTransform.Matrix(array.copyOfRange(offset + 1, offset + 7))
        }
        else -> error("Internal ID corrupted in transform handling")
    }
}