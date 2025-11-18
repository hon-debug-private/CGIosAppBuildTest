package com.honatsugiexp.canvasegg.data.svg.parser.command

import com.fleeksoft.ksoup.nodes.Element
import com.honatsugiexp.cssparser.ElementStyleController

class LinearGradientCommand(
    override val env: RenderEnv,
    sealedChildren: MutableList<StopCommand>,
    override val parent: RenderCommand?,
    override val element: Element
): GradientCommand(env, sealedChildren, parent), DefCommand, ElementCommand {
    override val controller: ElementStyleController = controller()
}