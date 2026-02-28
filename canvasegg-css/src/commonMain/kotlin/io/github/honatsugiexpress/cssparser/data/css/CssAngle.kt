package io.github.honatsugiexpress.cssparser.data.css

import io.github.honatsugiexpress.cssparser.data.css.CssAngle.Degrees
import io.github.honatsugiexpress.cssparser.data.css.CssAngle.Gradian
import io.github.honatsugiexpress.cssparser.data.css.CssAngle.Radian
import io.github.honatsugiexpress.cssparser.data.css.CssAngle.Turn
import io.github.honatsugiexpress.cssparser.data.css.CssAngle.Unknown
import kotlinx.serialization.Serializable
import kotlin.math.PI

@Serializable
sealed class CssAngle {
    @Serializable
    data class Degrees(val value: Float): CssAngle() {
        override fun toDegreesValue(): Float = value
    }
    @Serializable
    data class Radian(val value: Float): CssAngle() {
        override fun toDegreesValue(): Float = (value * 180f / PI).toFloat()
    }
    @Serializable
    data class Gradian(val value: Float): CssAngle() {
        override fun toDegreesValue(): Float = value * 0.9f
    }
    @Serializable
    data class Turn(val value: Float): CssAngle() {
        override fun toDegreesValue(): Float = value * 360f
    }
    @Serializable
    data object Unknown: CssAngle() {
        override fun toDegreesValue(): Float = 0f
    }
    abstract fun toDegreesValue(): Float
}

fun CssAngle(valueString: String, unit: String): CssAngle {
    val rawValue = valueString.toFloatOrNull() ?: 0f
    val value = rawValue % 360
    return when (unit) {
        "deg" -> Degrees(value)
        "rad" -> Radian(value)
        "grad" -> Gradian(value)
        "turn" -> Turn(value)
        "" -> Degrees(value)
        else -> Unknown
    }
}