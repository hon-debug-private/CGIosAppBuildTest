@file:Suppress("NOTHING_TO_INLINE")

package com.honatsugiexp.canvasegg.data.svg.parser.command.element.ktx

import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import com.honatsugiexp.canvasegg.data.svg.parser.command.ElementCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.element.ktx.attrOrStyleOrNull
import com.honatsugiexp.canvasegg.data.svg.type.SvgLength
import com.honatsugiexp.canvasegg.data.svg.type.SvgNormalizedFloat
import com.honatsugiexp.canvasegg.data.svg.type.SvgNormalizedFloat.Companion.invoke
import com.honatsugiexp.canvasegg.data.svg.type.SvgPaint

inline fun ElementCommand.stroke() = controller.attrOrStyleOrNull("stroke")?.let {
    SvgPaint.resolve(it)
}

inline fun ElementCommand.strokeDashOffset() = SvgLength(controller.attrOrStyleOrNull("stroke-dashoffset"))

inline fun ElementCommand.strokeLineCap() = when (controller.attrOrStyleOrNull("stroke-dashoffset")) {
    "butt" -> StrokeCap.Butt
    "round" -> StrokeCap.Round
    "square" -> StrokeCap.Square
    else -> StrokeCap.Butt
}

inline fun ElementCommand.strokeLineJoin() = when (controller.attrOrStyleOrNull("stroke-linejoin")) {
    "arcs", "round" -> StrokeJoin.Round
    "bevel" -> StrokeJoin.Bevel
    "miter", "miter-clip" -> StrokeJoin.Miter
    else -> StrokeJoin.Miter
}

inline fun ElementCommand.strokeMiterLimit(default: Float = 4f) =
    controller.attrOrStyleOrNull("stroke-miterlimit")?.toFloatOrNull() ?: default

inline fun ElementCommand.strokeOpacity() = SvgNormalizedFloat(
    controller.attrOrStyleOrNull("stroke-opacity") ?: "1"
)

inline fun ElementCommand.strokeWidth() = SvgLength(controller.attrOrStyleOrNull("stroke-width") ?: "1px")