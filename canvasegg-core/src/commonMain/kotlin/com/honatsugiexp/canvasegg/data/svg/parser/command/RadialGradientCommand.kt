package com.honatsugiexp.canvasegg.data.svg.parser.command

import com.fleeksoft.ksoup.nodes.Element
import com.honatsugiexp.canvasegg.common.util.attrOrNull
import com.honatsugiexp.canvasegg.data.svg.type.SvgLength
import com.honatsugiexp.cssparser.ElementStyleController

class RadialGradientCommand(
    override val env: RenderEnv,
    sealedChildren: MutableList<StopCommand>,
    override val parent: RenderCommand?,
    override val element: Element
): GradientCommand(env, sealedChildren, parent), DefCommand, ElementCommand {
    override val controller: ElementStyleController = controller()
}

inline val RadialGradientCommand.cx
    get() = SvgLength(element.attr("cx"))

inline val RadialGradientCommand.cy
    get() = SvgLength(element.attr("cy"))

inline val RadialGradientCommand.r
    get() = SvgLength(element.attr("r"))

inline val RadialGradientCommand.fx
    get() = SvgLength(element.attrOrNull("fx") ?: element.attr("cx"))

inline val RadialGradientCommand.fy
    get() = SvgLength(element.attrOrNull("fy") ?: element.attr("cy"))

inline val RadialGradientCommand.fr
    get() = SvgLength(element.attrOrNull("fr") ?: element.attr("fr"))