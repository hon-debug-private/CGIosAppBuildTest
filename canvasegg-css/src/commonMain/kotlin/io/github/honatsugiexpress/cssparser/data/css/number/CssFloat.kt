@file:Suppress("NOTHING_TO_INLINE")

package io.github.honatsugiexpress.cssparser.data.css.number

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

fun CssFloat(value: String?, defaultValue: Float = 0f) = if (value == null) {
    CssNumberFloat.Zero
} else if (value.endsWith("%")) {
    CssPercentFloat(value.removeSuffix("%").toFloatOrNull() ?: defaultValue)
} else if (value.toFloatOrNull() != null) {
    CssNumberFloat(value.toFloatOrNull() ?: defaultValue)
} else {
    CssIdentifierFloat(value, defaultValue)
}

inline fun CssFloat(value: Float) = CssNumberFloat(value)

inline fun CssPercentFloat(value: String?, defaultValue: Float = 0f) = if (value == null || !value.endsWith("%")) {
    CssPercentFloat.Zero
} else {
    CssPercentFloat(value.removeSuffix("%").toFloatOrNull() ?: defaultValue)
}

@Serializable
sealed interface CssFloat

@JvmInline
@Serializable
value class CssNumberFloat(val value: Float): CssFloat {
    override fun toString(): String = value.toString()
    companion object {
        val Zero = CssNumberFloat(0f)
    }
}

@JvmInline
@Serializable
value class CssPercentFloat(val percent: Float): CssFloat {
    override fun toString(): String = "$percent%"
    companion object {
        val Zero = CssPercentFloat(0f)
    }
}

data class CssIdentifierFloat(val keyword: String, val defaultValue: Float): CssFloat {
    override fun toString(): String = "none"
}

inline fun CssFloat.toFloat(max: Float = 1f) = when (this) {
    is CssNumberFloat -> this.toFloat(max)
    is CssPercentFloat -> this.toFloat(max)
    is CssIdentifierFloat -> this.defaultValue
}
inline fun CssNumberFloat.toFloat(max: Float) = value.coerceAtMost(max)
inline fun CssPercentFloat.toFloat(max: Float) = percent * max / 100f