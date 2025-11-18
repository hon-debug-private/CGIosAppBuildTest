package com.honatsugiexp.canvasegg.data.svg.type

import kotlin.jvm.JvmInline

@JvmInline
value class SvgTransformList(val transforms: List<SvgTransform>) {
    override fun toString(): String {
        return transforms.toString()
    }
    companion object {
        val Empty = SvgTransformList(emptyList())
        fun parse(content: String?): SvgTransformList {
            if (content == null) return Empty
            val regex = "([a-z]+)\\(([^)]*)\\)".toRegex()

            val transformFunctionStrings = regex.findAll(content)
                .map { it.groupValues[0] }
                .toList()

            val parsedTransforms = transformFunctionStrings.mapNotNull { functionString ->
                SvgTransform.parse(functionString)
            }

            return SvgTransformList(parsedTransforms)
        }
    }
}