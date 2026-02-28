@file:Suppress("NOTHING_TO_INLINE")

package io.github.honatsugiexpress.canvasegg.data.svg.type

import androidx.compose.ui.graphics.Path
import kotlin.jvm.JvmInline

@JvmInline
value class SvgBoundingBox @PublishedApi internal constructor(
    @PublishedApi internal val values: FloatArray
) {
    inline val x get() = values[0]
    inline val y get() = values[1]
    inline val width get() = values[2]
    inline val height get() = values[3]
    inline val strokeWidth: Float get() = if (values.size >= 5) values[4] else 0f
    inline val visualX get() = x - (strokeWidth / 2f)
    inline val visualY get() = y - (strokeWidth / 2f)
    inline val visualWidth get() = width + strokeWidth
    inline val visualHeight get() = height + strokeWidth
    companion object {
        fun fromString(content: String): SvgBoundingBox {
            val parts = content.split("\\s+".toRegex())
            val boundingBox = floatArrayOf(
                parts[0].toFloatOrNull() ?: 0f,
                parts[1].toFloatOrNull() ?: 0f,
                parts[2].toFloatOrNull() ?: 0f,
                parts[3].toFloatOrNull() ?: 0f
            )
            return SvgBoundingBox(boundingBox)
        }

        fun fromPath(path: Path): SvgBoundingBox {
            val rect = path.getBounds()
            return SvgBoundingBox(floatArrayOf(rect.left, rect.top, rect.width, rect.height))
        }

        inline fun fromValues(
            x: Float,
            y: Float,
            width: Float,
            height: Float
        ) = SvgBoundingBox(floatArrayOf(x, y, width, height))

        inline fun fromValues(
            x: Float,
            y: Float,
            width: Float,
            height: Float,
            strokeWidth: Float
        ) = SvgBoundingBox(floatArrayOf(x, y, width, height, strokeWidth))

        val Zero = SvgBoundingBox(FloatArray(4))
    }
}

data class SvgLengthBoundingBox(
    val x: SvgLength,
    val y: SvgLength,
    val width: SvgLength,
    val height: SvgLength
) {
    companion object {
        fun fromString(content: String): SvgLengthBoundingBox {
            val parts = content.split("\\s+".toRegex())
            return SvgLengthBoundingBox(
                SvgLength(
                    parts[0]
                ),
                SvgLength(
                    parts[1]
                ),
                SvgLength(
                    parts[2]
                ),
                SvgLength(
                    parts[3]
                )
            )
        }
    }
}