@file:Suppress("NOTHING_TO_INLINE")

package com.honatsugiexp.canvasegg.data.svg.parser.command

import com.fleeksoft.ksoup.nodes.Element
import com.honatsugiexp.canvasegg.data.svg.type.SvgLength
import com.honatsugiexp.cssparser.ElementStyleController

class LinearGradientCommand(
    override val env: RenderEnv,
    sealedChildren: MutableList<StopCommand>,
    override val parent: RenderCommand?,
    override val element: Element
): GradientCommand(env, sealedChildren, parent), DefCommand, ElementCommand {
    override val controller: ElementStyleController = controller()
}

inline val LinearGradientCommand.x1
    get() = SvgLength(element.attr("x1"))

inline val LinearGradientCommand.x2
    get() = SvgLength(element.attr("x2"))


inline val LinearGradientCommand.y1
    get() = SvgLength(element.attr("y1"))


inline val LinearGradientCommand.y2
    get() = SvgLength(element.attr("y2"))