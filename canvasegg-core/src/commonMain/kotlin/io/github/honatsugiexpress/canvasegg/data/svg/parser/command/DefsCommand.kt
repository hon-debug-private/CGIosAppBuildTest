package io.github.honatsugiexpress.canvasegg.data.svg.parser.command

import com.fleeksoft.ksoup.nodes.Element
import io.github.honatsugiexpress.canvasegg.data.css.ElementStyleData

class DefsCommand(
    override val env: RenderEnv,
    override val children: MutableList<RenderCommand>,
    override val parent: RenderCommand?,
    override val element: Element
): HasChildCommand(env, children, parent), ElementCommand {
    override val styleData: ElementStyleData = styleData()
}

sealed interface DefCommand