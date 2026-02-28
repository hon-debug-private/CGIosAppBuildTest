package io.github.honatsugiexpress.canvasegg.data.svg.parser.command

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.fleeksoft.ksoup.nodes.Element
import io.github.honatsugiexpress.canvasegg.data.css.ElementStyleData
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.xLenEnv
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.yLenEnv
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.transform.applyCommand
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgLength
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgPaint
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgStrokeData
import io.github.honatsugiexpress.canvasegg.data.svg.type.entity
import io.github.honatsugiexpress.canvasegg.data.svg.type.toPxValue

class EllipseCommand(
    override val env: RenderEnv,
    override val parent: RenderCommand?,
    override val element: Element
) : RenderCommand(env, parent),
    DrawableCommand,
    ElementCommand {
    override val styleData: ElementStyleData = styleData()
    override fun draw(drawScope: DrawScope) {
        val cx = SvgLength(styleData.attrOrStyleOrNull("cx")).toPxValue(xLenEnv)
        val cy = SvgLength(styleData.attrOrStyleOrNull("cy")).toPxValue(xLenEnv)
        val rx = SvgLength(styleData.attrOrStyleOrNull("rx")).toPxValue(yLenEnv)
        val ry = SvgLength(styleData.attrOrStyleOrNull("ry")).toPxValue(yLenEnv)
        val fill = SvgPaint.resolve(this, "fill").entity(this)
        val strokeData = SvgStrokeData.resolve(this, env.density)
        when (fill) {
            is SvgPaint.Color -> drawScope.drawOval(
                color = fill.color,
                topLeft = Offset(cx - rx, cy - ry),
                size = Size(rx * 2, ry * 2)
            )

            is SvgPaint.BrushPaint -> drawScope.drawOval(
                brush = fill.toBrush(),
                topLeft = Offset(cx - rx, cy - ry),
                size = Size(rx * 2, ry * 2)
            )

            else -> {}
        }
        when (val strokePaint = strokeData?.paint) {
            is SvgPaint.Color -> drawScope.drawOval(
                color = strokePaint.color,
                topLeft = Offset(cx - rx, cy - ry),
                size = Size(rx * 2, ry * 2),
                style = strokeData.stroke
            )

            is SvgPaint.BrushPaint -> drawScope.drawOval(
                brush = strokePaint.toBrush(),
                topLeft = Offset(cx - rx, cy - ry),
                size = Size(rx * 2, ry * 2),
                style = strokeData.stroke
            )

            else -> {}
        }
    }

    override fun path(): Path {
        val cx = SvgLength(styleData.attrOrStyleOrNull("cx")).toPxValue(xLenEnv)
        val cy = SvgLength(styleData.attrOrStyleOrNull("cy")).toPxValue(xLenEnv)
        val rx = SvgLength(styleData.attrOrStyleOrNull("rx")).toPxValue(yLenEnv)
        val ry = SvgLength(styleData.attrOrStyleOrNull("ry")).toPxValue(yLenEnv)
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
                Rect(Offset(cx - rx, cy - ry), Size(rx * 2, ry * 2))
            )
        }
    }
}