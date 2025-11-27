package com.honatsugiexp.canvasegg.data.svg.parser.command

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.fleeksoft.ksoup.nodes.Element
import com.honatsugiexp.canvasegg.data.svg.parser.command.ktx.element.lenEnv
import com.honatsugiexp.canvasegg.data.svg.parser.command.transform.applyCommand
import com.honatsugiexp.canvasegg.data.svg.type.SvgLength
import com.honatsugiexp.canvasegg.data.svg.type.SvgPaint
import com.honatsugiexp.canvasegg.data.svg.type.SvgStrokeData
import com.honatsugiexp.canvasegg.data.svg.type.entity
import com.honatsugiexp.canvasegg.data.svg.type.toPxValue
import com.honatsugiexp.cssparser.ElementStyleController

class CircleCommand(
    override val env: RenderEnv,
    override val parent: RenderCommand?,
    override val element: Element
): HasParentCommand(env, parent), DrawableCommand, ElementCommand {
    override val controller: ElementStyleController = controller()
    override fun draw(drawScope: DrawScope) {
        val controller = ElementStyleController(element)
        controller.parseDocumentStyles()
        val cx = SvgLength(controller.attrOrStyleOrNull("cx")).toPxValue(lenEnv)
        val cy = SvgLength(controller.attrOrStyleOrNull("cy")).toPxValue(lenEnv)
        val r = SvgLength(controller.attrOrStyleOrNull("r")).toPxValue(lenEnv)
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
        val controller = ElementStyleController(element)
        controller.parseDocumentStyles()
        val cx = SvgLength(controller.attrOrStyleOrNull("cx")).toPxValue(lenEnv)
        val cy = SvgLength(controller.attrOrStyleOrNull("cy")).toPxValue(lenEnv)
        val r = SvgLength(controller.attrOrStyleOrNull("r")).toPxValue(lenEnv)
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