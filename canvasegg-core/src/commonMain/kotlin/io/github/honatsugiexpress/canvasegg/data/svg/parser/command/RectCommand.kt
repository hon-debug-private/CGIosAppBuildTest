package io.github.honatsugiexpress.canvasegg.data.svg.parser.command

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
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
import io.github.honatsugiexpress.canvasegg.data.svg.type.toScreenValue

data class RectCommand(
    override val env: RenderEnv,
    override val parent: RenderCommand?,
    override val element: Element
): RenderCommand(env, parent),
    DrawableCommand,
    ElementCommand,
    HasDescCommand {
    override val styleData: ElementStyleData = styleData()
    override fun draw(drawScope: DrawScope) {
        val x = SvgLength(styleData.attrOrStyleOrNull("x")).toScreenValue(xLenEnv, env.density)
        val y = SvgLength(styleData.attrOrStyleOrNull("y")).toScreenValue(yLenEnv, env.density)
        val width = SvgLength(styleData.attrOrStyleOrNull("width")).toScreenValue(xLenEnv, env.density)
        val height = SvgLength(styleData.attrOrStyleOrNull("height")).toScreenValue(yLenEnv, env.density)
        val rx = SvgLength(styleData.attrOrStyleOrNull("rx")).toScreenValue(xLenEnv, env.density)
        val ry = SvgLength(styleData.attrOrStyleOrNull("rx")).toScreenValue(yLenEnv, env.density)
        val fill = SvgPaint.resolve(this, "fill").entity(this)
        val strokeData = SvgStrokeData.resolve(this, env.density)
        if (rx <= 0f && ry <= 0f) {
            when (fill) {
                is SvgPaint.Color -> drawScope.drawRect(
                    color = fill.color,
                    topLeft = Offset(x, y),
                    size = Size(width, height)
                )
                is SvgPaint.BrushPaint -> drawScope.drawRect(
                    brush = fill.toBrush(),
                    topLeft = Offset(x, y),
                    size = Size(width, height)
                )
                else -> {}
            }
            when (val strokePaint = strokeData?.paint) {
                is SvgPaint.Color -> drawScope.drawRect(
                    color = strokePaint.color,
                    topLeft = Offset(x, y),
                    size = Size(width, height),
                    style = strokeData.stroke
                )
                is SvgPaint.BrushPaint -> drawScope.drawRect(
                    brush = strokePaint.toBrush(),
                    topLeft = Offset(x, y),
                    size = Size(width, height),
                    style = strokeData.stroke
                )
                else -> {}
            }
        } else {
            when (fill) {
                is SvgPaint.Color -> drawScope.drawRoundRect(
                    color = fill.color,
                    topLeft = Offset(x, y),
                    size = Size(width, height),
                    cornerRadius = CornerRadius(rx, ry)
                )
                is SvgPaint.BrushPaint -> drawScope.drawRoundRect(
                    brush = fill.toBrush(),
                    topLeft = Offset(x, y),
                    size = Size(width, height),
                    cornerRadius = CornerRadius(rx, ry)
                )
                else -> {}
            }
            strokeData?.let {
                when (val strokePaint = it.paint) {
                    is SvgPaint.Color -> drawScope.drawRoundRect(
                        color = strokePaint.color,
                        topLeft = Offset(x, y),
                        size = Size(width, height),
                        cornerRadius = CornerRadius(rx, ry),
                        style = strokeData.stroke
                    )
                    is SvgPaint.BrushPaint -> drawScope.drawRoundRect(
                        brush = strokePaint.toBrush(),
                        topLeft = Offset(x, y),
                        size = Size(width, height),
                        cornerRadius = CornerRadius(rx, ry),
                        style = strokeData.stroke
                    )
                    else -> {}
                }
            }
        }
    }

    override fun path(): Path {
        val x = SvgLength(styleData.attrOrStyleOrNull("x")).toScreenValue(xLenEnv, env.density)
        val y = SvgLength(styleData.attrOrStyleOrNull("y")).toScreenValue(yLenEnv, env.density)
        val width = SvgLength(styleData.attrOrStyleOrNull("width")).toScreenValue(xLenEnv, env.density)
        val height = SvgLength(styleData.attrOrStyleOrNull("height")).toScreenValue(yLenEnv, env.density)
        val rx = SvgLength(styleData.attrOrStyleOrNull("rx")).toScreenValue(xLenEnv, env.density)
        val ry = SvgLength(styleData.attrOrStyleOrNull("rx")).toScreenValue(yLenEnv, env.density)
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
            if (rx == 0f && ry == 0f) {
                addRect(
                    Rect(
                        Offset(x, y),
                        Size(width, height)
                    )
                )
            } else {
                addRoundRect(
                    RoundRect(
                        Rect(
                            Offset(x, y),
                            Size(width, height)
                        ),
                        cornerRadius = CornerRadius(rx, ry)
                    )
                )
            }
        }
    }
}