@file:Suppress("NOTHING_TO_INLINE")

package com.honatsugiexp.canvasegg.data.svg.type

value class SvgBoundingBox @PublishedApi internal constructor(@PublishedApi internal val values: FloatArray) {
    inline val x
        get() = values[0]
    inline val y
        get() = values[1]
    inline val width
        get() = values[2]
    inline val height
        get() = values[3]
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
        inline fun fromValues(
            x: Float,
            y: Float,
            width: Float,
            height: Float
        ) = SvgBoundingBox(
            floatArrayOf(x, y, width, height)
        )
    }
}