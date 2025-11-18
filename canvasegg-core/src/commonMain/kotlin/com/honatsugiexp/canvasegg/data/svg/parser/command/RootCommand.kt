package com.honatsugiexp.canvasegg.data.svg.parser.command

import com.fleeksoft.ksoup.nodes.Element
import com.honatsugiexp.cssparser.ElementStyleController

class RootCommand(
    override val env: RenderEnv,
    override val children: MutableList<RenderCommand>,
    override val parent: RenderCommand?,
    override val element: Element
): HasChildCommand(env, children, parent), ElementCommand {
    override val controller: ElementStyleController = controller()
}