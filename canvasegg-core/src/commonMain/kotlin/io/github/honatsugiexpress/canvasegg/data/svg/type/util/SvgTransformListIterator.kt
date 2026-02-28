package io.github.honatsugiexpress.canvasegg.data.svg.type.util

import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgTransform

class SvgTransformListIterator(private val values: FloatArray): Iterator<SvgTransform> {
    private var offset = 0
    override fun hasNext(): Boolean {
        return offset < values.size
    }

    override fun next(): SvgTransform {
        val pair = SvgTransform.fromArray(values, offset)
        offset = pair.first
        return pair.second
    }
}