package io.github.honatsugiexpress.canvasegg.data.svg.type

import io.github.honatsugiexpress.canvasegg.common.log.CanvasEggLogger
import kotlin.math.PI

sealed class SvgAngle {
    data class Degrees(val value: Float): SvgAngle() {
        override fun toDegreesValue(): Float = value
    }
    data class Radian(val value: Float): SvgAngle() {
        override fun toDegreesValue(): Float = (value * 180f / PI).toFloat()
    }
    data class Gradian(val value: Float): SvgAngle() {
        override fun toDegreesValue(): Float = value * 0.9f
    }
    data object Unknown: SvgAngle() {
        override fun toDegreesValue(): Float = 0f
    }
    abstract fun toDegreesValue(): Float
}

fun SvgAngle(value: String): SvgAngle = try {
    when {
        value.endsWith("deg") -> SvgAngle.Degrees(value.removeSuffix("deg").toFloat())
        value.endsWith("rad") -> SvgAngle.Radian(value.removeSuffix("rad").toFloat())
        value.endsWith("grad") -> SvgAngle.Gradian(value.removeSuffix("grad").toFloat())
        value.toFloatOrNull() != null -> SvgAngle.Degrees(value.toFloat())
        else -> SvgAngle.Unknown
    }
} catch (e: NumberFormatException) {
    CanvasEggLogger.warnThrowable(e)
    SvgAngle.Unknown
}