package com.honatsugiexp.canvasegg.data.svg.parser.command

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.fleeksoft.ksoup.nodes.Element
import com.honatsugiexp.canvasegg.data.svg.parser.command.transform.applyCommand
import com.honatsugiexp.canvasegg.data.svg.type.SvgLength
import com.honatsugiexp.canvasegg.data.svg.type.SvgPaint
import com.honatsugiexp.canvasegg.data.svg.type.SvgStrokeData
import com.honatsugiexp.canvasegg.data.svg.type.entity
import com.honatsugiexp.canvasegg.data.svg.type.toBrush
import com.honatsugiexp.canvasegg.data.svg.type.toPxValue
import com.honatsugiexp.cssparser.ElementStyleController

data class RectCommand(
    override val env: RenderEnv,
    override val parent: RenderCommand?,
    override val element: Element
): HasParentCommand(env, parent), DrawableCommand, ElementCommand {
    override val controller: ElementStyleController = controller()
    override fun draw(drawScope: DrawScope) {
        val controller = ElementStyleController(element)
        controller.parseDocumentStyles()
        val lenEnv = SvgLength.Env.fromCommand(this, env.density)
        val x = SvgLength(controller.attrOrStyleOrNull("x")).toPxValue(lenEnv)
        val y = SvgLength(controller.attrOrStyleOrNull("y")).toPxValue(lenEnv)
        val width = SvgLength(controller.attrOrStyleOrNull("width")).toPxValue(lenEnv)
        val height = SvgLength(controller.attrOrStyleOrNull("height")).toPxValue(lenEnv)
        val rx = SvgLength(controller.attrOrStyleOrNull("rx")).toPxValue(lenEnv)
        val ry = SvgLength(controller.attrOrStyleOrNull("rx")).toPxValue(lenEnv)
        val fill = SvgPaint.resolve(this, "fill").entity(this)
        val strokeData = SvgStrokeData.resolve(this, env.density)
        if (rx <= 0f && ry <= 0f) {
            when (fill) {
                is SvgPaint.Color -> drawScope.drawRect(
                    color = fill.color,
                    topLeft = Offset(x, y),
                    size = Size(width, height)
                )
                is SvgPaint.Gradient -> drawScope.drawRect(
                    brush = fill.toBrush(
                        Rect(Offset(x, y), Size(width, height))
                    ),
                    topLeft = Offset(x, y),
                    size = Size(width, height)
                )
                else -> {}
            }
            strokeData?.let {
                when (val strokePaint = it.paint) {
                    is SvgPaint.Color -> drawScope.drawRect(
                        color = strokePaint.color,
                        topLeft = Offset(x, y),
                        size = Size(width, height),
                        style = strokeData.stroke
                    )
                    is SvgPaint.Gradient -> drawScope.drawRect(
                        brush = strokePaint.toBrush(
                            Rect(Offset(x, y), Size(width, height))
                        ),
                        topLeft = Offset(x, y),
                        size = Size(width, height),
                        style = strokeData.stroke
                    )
                    else -> {}
                }
            }
        } else {
            when (fill) {
                is SvgPaint.Color -> drawScope.drawRoundRect(
                    color = fill.color,
                    topLeft = Offset(x, y),
                    size = Size(width, height),
                    cornerRadius = CornerRadius(rx, ry)
                )
                is SvgPaint.Gradient -> drawScope.drawRoundRect(
                    brush = fill.toBrush(
                        Rect(Offset(x, y), Size(width, height))
                    ),
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
                    is SvgPaint.Gradient -> drawScope.drawRoundRect(
                        brush = strokePaint.toBrush(
                            Rect(Offset(x, y), Size(width, height))
                        ),
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
        val controller = ElementStyleController(element)
        controller.parseDocumentStyles()
        val lenEnv = SvgLength.Env.fromCommand(this, env.density)
        val x = SvgLength(controller.attrOrStyleOrNull("x")).toPxValue(lenEnv)
        val y = SvgLength(controller.attrOrStyleOrNull("y")).toPxValue(lenEnv)
        val width = SvgLength(controller.attrOrStyleOrNull("width")).toPxValue(lenEnv)
        val height = SvgLength(controller.attrOrStyleOrNull("height")).toPxValue(lenEnv)
        val rx = SvgLength(controller.attrOrStyleOrNull("rx")).toPxValue(lenEnv)
        val ry = SvgLength(controller.attrOrStyleOrNull("rx")).toPxValue(lenEnv)
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