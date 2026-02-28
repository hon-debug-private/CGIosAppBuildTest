package io.github.honatsugiexpress.canvasegg.data.svg.type

import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.DrawableCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ElementCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.RenderCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.attrOrStyleOrNull
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.hasAttr
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.lenEnvWithResolver
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.strokeDashOffset
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.strokeLineCap
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.strokeLineJoin
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.strokeMiterLimit
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.strokeOpacity
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.strokeWidth
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.root
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.viewBox
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.viewport
import io.github.honatsugiexpress.canvasegg.data.svg.type.entity
import io.github.honatsugiexpress.canvasegg.data.svg.type.toPxValue
import kotlin.math.sqrt

data class SvgStrokeData(
    val paint: SvgPaint,
    val stroke: Stroke,
    val alpha: SvgNormalizedFloat
) {
    companion object {
        fun resolve(command: DrawableCommand, density: Float): SvgStrokeData? {
            command as RenderCommand
            val elemCommand = (command as? ElementCommand) ?: return null
            val lenEnv = command.lenEnvWithResolver { value ->
                val viewBox = command.viewport().boundingBox()
                val diagonal = sqrt(viewBox.width * viewBox.width + viewBox.height * viewBox.height)

                val normalizedDiagonal = diagonal / sqrt(2f)
                (value / 100) * normalizedDiagonal
            }
            if (!elemCommand.hasAttr("stroke")) return null
            val paint = SvgPaint.resolve(elemCommand, "stroke").entity(command)
            val dashArray = elemCommand.attrOrStyleOrNull("stroke-dasharray")
                ?.split(" ")
                ?.filter { it.isNotBlank() }
                ?.map {
                    SvgLength(
                        it
                    ).toPxValue(lenEnv) * density
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
            val dashOffset = elemCommand.strokeDashOffset
            val lineCap = elemCommand.strokeLineCap
            val lineJoin = elemCommand.strokeLineJoin
            val miterLimit = elemCommand.strokeMiterLimit()
            val opacity = elemCommand.strokeOpacity
            val width = elemCommand.strokeWidth
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
        fun dashPathEffect(command: DrawableCommand, density: Float): Pair<FloatArray, Float>? {
            command as RenderCommand
            val elemCommand = (command as? ElementCommand) ?: return null
            val lenEnv = command.lenEnvWithResolver { value ->
                val viewBox = command.viewport().boundingBox()
                val diagonal = sqrt(viewBox.width * viewBox.width + viewBox.height * viewBox.height)

                val normalizedDiagonal = diagonal / sqrt(2f)
                (value / 100) * normalizedDiagonal
            }
            val dashArray = elemCommand.attrOrStyleOrNull("stroke-dasharray")
                ?.split(" ")
                ?.filter { it.isNotBlank() }
                ?.map {
                    SvgLength(
                        it
                    ).toPxValue(lenEnv) * density
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
            val dashOffset = elemCommand.strokeDashOffset
            return validDashArray?.let {
                it to dashOffset.toPxValue(lenEnv) * density
            }
        }
    }
}