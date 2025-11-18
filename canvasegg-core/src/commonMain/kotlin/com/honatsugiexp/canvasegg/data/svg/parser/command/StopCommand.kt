package com.honatsugiexp.canvasegg.data.svg.parser.command

import com.fleeksoft.ksoup.nodes.Element
import com.honatsugiexp.canvasegg.common.util.attrOrNull
import com.honatsugiexp.canvasegg.data.svg.type.SvgNormalizedFloat
import com.honatsugiexp.canvasegg.data.svg.type.SvgPaint
import com.honatsugiexp.cssparser.ElementStyleController

class StopCommand(
    override val env: RenderEnv,
    override val element: Element
): RenderCommand(env), ElementCommand {
    override val controller = controller()
    val offset: SvgNormalizedFloat
        get() = SvgNormalizedFloat(element.attrOrNull("offset"))
    val color: SvgPaint.Color
        get() = SvgPaint.Color.fromString(controller.attrOrStyleOrNull("stop-color") ?: "#000")
    val opacity: SvgNormalizedFloat
        get() = SvgNormalizedFloat(controller.attrOrStyleOrNull("stop-opacity")?.toFloatOrNull() ?: 1f)
}