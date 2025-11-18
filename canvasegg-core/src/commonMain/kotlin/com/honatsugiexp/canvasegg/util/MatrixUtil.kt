package com.honatsugiexp.canvasegg.util

import androidx.compose.ui.graphics.Matrix

fun Matrix.preConcat(other: Matrix) {
    val currentValues = values.clone()
    val newValues = other.values

    val result = FloatArray(16)

    for (i in 0 until 4) {
        for (j in 0 until 4) {
            var sum = 0f
            for (k in 0 until 4) {
                sum += newValues[i * 4 + k] * currentValues[k * 4 + j]
            }
            result[i * 4 + j] = sum
        }
    }

    setFrom(result)
}

fun Matrix.setFrom(newValues: FloatArray) {
    val currentValues = values
    if (newValues.size != currentValues.size) return
    newValues.copyInto(currentValues)
}