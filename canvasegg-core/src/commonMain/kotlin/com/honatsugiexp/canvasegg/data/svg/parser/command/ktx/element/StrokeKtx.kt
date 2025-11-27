@file:Suppress("NOTHING_TO_INLINE")

package com.honatsugiexp.canvasegg.data.svg.parser.command.ktx.element

import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import com.honatsugiexp.canvasegg.data.svg.parser.command.ElementCommand
import com.honatsugiexp.canvasegg.data.svg.type.SvgLength
import com.honatsugiexp.canvasegg.data.svg.type.SvgNormalizedFloat
import com.honatsugiexp.canvasegg.data.svg.type.SvgPaint

inline val ElementCommand.stroke
    get() = controller.attrOrStyleOrNull("stroke")?.let {
        SvgPaint.resolve(it)
    }

inline val ElementCommand.strokeDashOffset
    get() = SvgLength(controller.attrOrStyleOrNull("stroke-dashoffset"))

inline val ElementCommand.strokeLineCap
    get() = when (controller.attrOrStyleOrNull("stroke-dashoffset")) {
        "butt" -> StrokeCap.Butt
        "round" -> StrokeCap.Round
        "square" -> StrokeCap.Square
        else -> StrokeCap.Butt
    }

inline val ElementCommand.strokeLineJoin
    get() = when (controller.attrOrStyleOrNull("stroke-linejoin")) {
        "arcs", "round" -> StrokeJoin.Round
        "bevel" -> StrokeJoin.Bevel
        "miter", "miter-clip" -> StrokeJoin.Miter
        else -> StrokeJoin.Miter
    }

inline fun ElementCommand.strokeMiterLimit(default: Float = 4f) =
    controller.attrOrStyleOrNull("stroke-miterlimit")?.toFloatOrNull() ?: default

inline val ElementCommand.strokeOpacity
    get() = SvgNormalizedFloat(
        controller.attrOrStyleOrNull("stroke-opacity") ?: "1"
    )

inline val ElementCommand.strokeWidth
    get() = SvgLength(controller.attrOrStyleOrNull("stroke-width") ?: "1px")