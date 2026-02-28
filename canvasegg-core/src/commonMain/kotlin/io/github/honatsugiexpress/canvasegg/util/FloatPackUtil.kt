@file:Suppress("NOTHING_TO_INLINE")

package io.github.honatsugiexpress.canvasegg.util

inline fun packFloatsToLong(float1: Float, float2: Float): Long {
    val high = float1.toRawBits().toLong() shl 32
    val low = float2.toRawBits().toLong() and 0xFFFFFFFFL
    return high or low
}

inline fun unpackFirstFloat(l: Long): Float {
    return Float.fromBits((l shr 32).toInt())
}

inline fun unpackSecondFloat(l: Long): Float {
    return Float.fromBits(l.toInt())
}