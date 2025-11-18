package com.honatsugiexp.canvasegg.data.svg.parser.command

import com.fleeksoft.ksoup.nodes.Element
import com.honatsugiexp.canvasegg.data.svg.type.SvgTagName
import com.honatsugiexp.canvasegg.data.svg.type.ktx.svgTagName

class RenderCommandResolver(val commands: MutableMap<Element, RenderCommand>, val env: RenderEnv) {
    fun resolve(current: Element): RenderCommand {
        return when (current.svgTagName()) {
            SvgTagName.GROUP -> GroupCommand(
                env,
                mutableListOf(),
                parentCommand(commands, current, env),
                current
            )

            SvgTagName.LINEAR_GRADIENT -> LinearGradientCommand(
                env,
                mutableListOf(),
                parentCommand(commands, current, env),
                current
            )

            SvgTagName.RADIAL_GRADIENT -> RadialGradientCommand(
                env,
                mutableListOf(),
                parentCommand(commands, current, env),
                current
            )

            SvgTagName.RECT -> RectCommand(
                env,
                parentCommand(commands, current, env),
                current
            )

            SvgTagName.SVG -> RootCommand(
                env,
                mutableListOf(),
                parentCommand(commands, current, env),
                current
            )

            else -> UnknownCommand(env)
        }
    }
    private fun parentCommand(commands: Map<Element, RenderCommand>, current: Element, env: RenderEnv): RenderCommand {
        val parent = current.parent() ?: return UnknownCommand(env)
        return commands.getOrElse(parent) {
            UnknownCommand(env)
        }
    }
}