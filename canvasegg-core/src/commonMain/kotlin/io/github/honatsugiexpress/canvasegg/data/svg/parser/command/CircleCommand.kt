package io.github.honatsugiexpress.canvasegg.data.svg.parser.command

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.fleeksoft.ksoup.nodes.Element
import io.github.honatsugiexpress.canvasegg.data.css.ElementStyleData
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.lenEnvWithResolver
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.xLenEnv
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.yLenEnv
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.transform.applyCommand
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgBoundingBox
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgLength
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgPaint
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgStrokeData
import io.github.honatsugiexpress.canvasegg.data.svg.type.entity
import io.github.honatsugiexpress.canvasegg.data.svg.type.toPxValue
import kotlin.math.sqrt

class CircleCommand(
    override val env: RenderEnv,
    override val parent: RenderCommand?,
    override val element: Element
): RenderCommand(env, parent),
    DrawableCommand,
    ElementCommand {
    override val styleData: ElementStyleData = styleData()
    override fun draw(drawScope: DrawScope) {
        val rLenEnv = lenEnvWithResolver { value ->
            val viewBox = viewport().boundingBox()
            val diagonal = sqrt(viewBox.width * viewBox.width + viewBox.height * viewBox.height)

            val normalizedDiagonal = diagonal / sqrt(2f)
            (value / 100) * normalizedDiagonal
        }
        val cx = SvgLength(styleData.attrOrStyleOrNull("cx")).toPxValue(xLenEnv)
        val cy = SvgLength(styleData.attrOrStyleOrNull("cy")).toPxValue(yLenEnv)
        val r = SvgLength(styleData.attrOrStyleOrNull("r")).toPxValue(rLenEnv)
        val fill = SvgPaint.resolve(this, "fill").entity(this)
        val strokeData = SvgStrokeData.resolve(this, env.density)
        when (fill) {
            is SvgPaint.Color -> drawScope.drawCircle(
                color = fill.color,
                radius = r,
                center = Offset(cx, cy)
            )
            is SvgPaint.BrushPaint -> drawScope.drawCircle(
                brush = fill.toBrush(),
                radius = r,
                center = Offset(cx, cy)
            )
            else -> {}
        }
        when (val strokePaint = strokeData?.paint) {
            is SvgPaint.Color -> drawScope.drawCircle(
                color = strokePaint.color,
                radius = r,
                center = Offset(cx, cy),
                style = strokeData.stroke
            )
            is SvgPaint.BrushPaint -> drawScope.drawCircle(
                brush = strokePaint.toBrush(),
                radius = r,
                center = Offset(cx, cy),
                style = strokeData.stroke
            )
            else -> {}
        }
    }

    override fun path(): Path {
        val rLenEnv = lenEnvWithResolver { value ->
            val viewBox = viewport().boundingBox()
            val diagonal = sqrt(viewBox.width * viewBox.width + viewBox.height * viewBox.height)

            val normalizedDiagonal = diagonal / sqrt(2f)
            (value / 100) * normalizedDiagonal
        }
        val cx = SvgLength(styleData.attrOrStyleOrNull("cx")).toPxValue(xLenEnv)
        val cy = SvgLength(styleData.attrOrStyleOrNull("cy")).toPxValue(yLenEnv)
        val r = SvgLength(styleData.attrOrStyleOrNull("r")).toPxValue(rLenEnv)
        return Path().apply {
            parentList()
                .asSequence()
                .filterIsInstance<HasTransformCommand>()
                .flatMap {
                    it.transformCommands()
                }
                .forEach {
                    applyCommand(it)
                }
            addOval(
                Rect(Offset(cx - r, cy - r), Size(r, r))
            )
        }
    }
}