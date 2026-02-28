package io.github.honatsugiexpress.cssparser.util

fun percentOrValueToValue(value: String?, defaultValue: Float = 0f): Float {
    if (value == null) return 0f
    val trimmed = value.trim()
    return if (trimmed.endsWith("%")) {
        val floatValue = trimmed.removeSuffix("%").toFloatOrNull()
        floatValue?.let { it / 100f } ?: defaultValue
    } else {
        trimmed.toFloatOrNull() ?: defaultValue
    }
}

fun percentToValue(value: String?, defaultValue: Float = 0f): Float {
    if (value == null) return 0f
    val trimmed = value.trim()
    val floatValue = trimmed.removeSuffix("%").toFloatOrNull()
    return floatValue?.let { it / 100f } ?: defaultValue
}


fun percentOrValueToValueRange(value: String?, max: Float, defaultValue: Float = 0f): Float {
    if (value == null) return 0f
    val factor = max / 100f
    val trimmed = value.trim()
    return if (trimmed.endsWith("%")) {
        val floatValue = trimmed.removeSuffix("%").toFloatOrNull()
        floatValue?.let { it * factor } ?: defaultValue
    } else {
        trimmed.toFloatOrNull() ?: defaultValue
    }
}

fun percentToValueRange(value: String?, max: Float, defaultValue: Float = 0f): Float {
    if (value == null) return 0f
    val factor = max / 100f
    val trimmed = value.trim()
    val floatValue = trimmed.removeSuffix("%").toFloatOrNull()
    return floatValue?.let { it * factor } ?: defaultValue
}