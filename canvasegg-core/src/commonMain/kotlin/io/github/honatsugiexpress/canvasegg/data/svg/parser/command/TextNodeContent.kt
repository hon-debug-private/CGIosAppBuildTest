package io.github.honatsugiexpress.canvasegg.data.svg.parser.command

import com.fleeksoft.ksoup.nodes.Node

class TextNodeContent(
    override val env: RenderEnv,
    override val parent: RenderCommand?,
    val node: Node
): RenderCommand(env, parent),
    TextContentCommand {
    override val textIndex: Int
        get() = node.siblingIndex()
}