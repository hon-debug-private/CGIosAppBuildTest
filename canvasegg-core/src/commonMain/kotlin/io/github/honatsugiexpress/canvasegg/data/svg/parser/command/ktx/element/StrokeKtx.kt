@file:Suppress("NOTHING_TO_INLINE")

package io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element

import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ElementCommand
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgLength
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgNormalizedFloat
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgPaint

inline val ElementCommand.stroke
    get() = styleData.attrOrStyleOrNull("stroke")?.let {
        SvgPaint.resolve(it, env)
    }

inline val ElementCommand.strokeDashOffset
    get() = SvgLength(styleData.attrOrStyleOrNull("stroke-dashoffset"))

inline val ElementCommand.strokeLineCap
    get() = when (styleData.attrOrStyleOrNull("stroke-dashoffset")) {
        "butt" -> StrokeCap.Butt
        "round" -> StrokeCap.Round
        "square" -> StrokeCap.Square
        else -> StrokeCap.Butt
    }

inline val ElementCommand.strokeLineJoin
    get() = when (styleData.attrOrStyleOrNull("stroke-linejoin")) {
        "arcs", "round" -> StrokeJoin.Round
        "bevel" -> StrokeJoin.Bevel
        "miter", "miter-clip" -> StrokeJoin.Miter
        else -> StrokeJoin.Miter
    }

inline fun ElementCommand.strokeMiterLimit(default: Float = 4f) =
    styleData.attrOrStyleOrNull("stroke-miterlimit")?.toFloatOrNull() ?: default

inline val ElementCommand.strokeOpacity
    get() = SvgNormalizedFloat(
        styleData.attrOrStyleOrNull("stroke-opacity") ?: "1"
    )

inline val ElementCommand.strokeWidth
    get() = SvgLength(
        styleData.attrOrStyleOrNull("stroke-width") ?: "1px"
    )