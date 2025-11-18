package com.honatsugiexp.canvasegg.data.svg.type

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class SvgNormalizedFloat private constructor(val value: Float) {
    companion object {
        operator fun invoke(floatValue: Float) = SvgNormalizedFloat(floatValue.coerceIn(0f, 1f))
        operator fun invoke(stringValue: String?): SvgNormalizedFloat {
            val value = if (stringValue == null) {
                0f
            } else {
                if (stringValue.endsWith("%")) {
                    stringValue.removeSuffix("%").toFloatOrNull()?.div(100f)
                } else {
                    stringValue.toFloatOrNull()
                } ?: 0f
            }
            return invoke(value)
        }
    }
}