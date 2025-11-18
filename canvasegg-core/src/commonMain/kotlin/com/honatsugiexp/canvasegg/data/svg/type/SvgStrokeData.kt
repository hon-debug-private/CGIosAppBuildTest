package com.honatsugiexp.canvasegg.data.svg.type

import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import com.honatsugiexp.canvasegg.data.svg.parser.command.ElementCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.RenderCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.element.ktx.attrOrStyleOrNull
import com.honatsugiexp.canvasegg.data.svg.parser.command.element.ktx.hasAttr
import com.honatsugiexp.canvasegg.data.svg.parser.command.element.ktx.strokeDashOffset
import com.honatsugiexp.canvasegg.data.svg.parser.command.element.ktx.strokeLineCap
import com.honatsugiexp.canvasegg.data.svg.parser.command.element.ktx.strokeLineJoin
import com.honatsugiexp.canvasegg.data.svg.parser.command.element.ktx.strokeMiterLimit
import com.honatsugiexp.canvasegg.data.svg.parser.command.element.ktx.strokeOpacity
import com.honatsugiexp.canvasegg.data.svg.parser.command.element.ktx.strokeWidth

data class SvgStrokeData(
    val paint: SvgPaint,
    val stroke: Stroke,
    val alpha: SvgNormalizedFloat
) {
    companion object {
        fun resolve(command: RenderCommand, density: Float): SvgStrokeData? {
            val elemCommand = (command as? ElementCommand) ?: return null
            val lenEnv = SvgLength.Env.fromCommand(command, density)
            if (!elemCommand.hasAttr("stroke")) return null
            val paint = SvgPaint.resolve(elemCommand, "stroke").entity(command)
            val dashArray = elemCommand.attrOrStyleOrNull("stroke-dasharray")
                ?.split(" ")
                ?.filter { it.isNotBlank() }
                ?.map {
                    SvgLength(it).toPxValue(lenEnv) * density
                }
                ?.toFloatArray()
            val validDashArray = dashArray?.let { array ->
                if (array.isEmpty() || array.any { it <= 0f || it.isNaN() || it.isInfinite() }) {
                    null
                } else if (array.size % 2 != 0) {
                    array + array
                } else {
                    array
                }
            }
            val dashOffset = elemCommand.strokeDashOffset()
            val lineCap = elemCommand.strokeLineCap()
            val lineJoin = elemCommand.strokeLineJoin()
            val miterLimit = elemCommand.strokeMiterLimit()
            val opacity = elemCommand.strokeOpacity()
            val width = elemCommand.strokeWidth()
            return SvgStrokeData(
                paint = paint,
                stroke = Stroke(
                    width = width.toPxValue(lenEnv) * density,
                    miter = miterLimit,
                    cap = lineCap,
                    join = lineJoin,
                    pathEffect = validDashArray?.let {
                        PathEffect.dashPathEffect(
                            intervals = it,
                            phase = dashOffset.toPxValue(lenEnv) * density
                        )
                    }
                ),
                alpha = opacity
            )
        }
    }
}