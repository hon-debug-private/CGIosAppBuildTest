package io.github.honatsugiexpress.canvasegg.data.svg.type

fun interface SvgPercentResolver {
    fun resolve(value: Float): Float
}