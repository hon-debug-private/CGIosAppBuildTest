package io.github.honatsugiexpress.canvasegg.data.svg.type

import io.github.honatsugiexpress.canvasegg.data.svg.type.util.SvgTransformListIterator
import io.github.honatsugiexpress.canvasegg.data.svg.type.util.fromArray
import io.github.honatsugiexpress.canvasegg.data.svg.type.util.size
import io.github.honatsugiexpress.canvasegg.data.svg.type.util.type
import io.github.honatsugiexpress.canvasegg.data.svg.type.util.writeData
import kotlin.jvm.JvmInline

@JvmInline
value class SvgTransformList(@PublishedApi internal val values: FloatArray): Iterable<SvgTransform> {
    override fun toString(): String {
        var isFirst = true
        return buildString {
            append("[")
            fastForEach {
                if (isFirst) {
                    append("$it")
                } else {
                    append(", ")
                    append("$it")
                }
                isFirst = false
            }
            append("]")
        }
    }

    override fun iterator(): Iterator<SvgTransform> {
        return SvgTransformListIterator(values)
    }
    companion object {
        val Empty = SvgTransformList(FloatArray(0))
        fun parse(content: String?): SvgTransformList {
            if (content == null) return Empty
            val regex = "([a-z]+)\\(([^)]*)\\)".toRegex()

            val transforms = regex.findAll(content)
                .mapNotNull { SvgTransform.parse(it.value) }
                .toList()

            if (transforms.isEmpty()) return Empty

            val totalSize = transforms.sumOf { it.size() + 1 }
            val array = FloatArray(totalSize)

            var offset = 0
            for (transform in transforms) {
                array[offset++] = transform.type().toFloat()
                offset = transform.writeData(array, offset)
            }

            return SvgTransformList(array)
        }
    }
}

inline fun SvgTransformList.fastForEach(action: (SvgTransform) -> Unit) {
    var offset = 0
    while (offset < values.size) {
        val (nextOffset, transform) = SvgTransform.fromArray(values, offset)
        action(transform)
        offset = nextOffset
    }
}