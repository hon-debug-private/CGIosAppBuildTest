package io.github.honatsugiexpress.canvasegg.data.svg.parser.command

import com.fleeksoft.ksoup.nodes.Element
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgTagName
import io.github.honatsugiexpress.canvasegg.data.svg.type.util.ktx.svgTagName

class RenderCommandResolver(val commands: MutableMap<Element, RenderCommand>, val env: RenderEnv) {
    fun resolve(current: Element): RenderCommand {
        return when (current.svgTagName()) {
            SvgTagName.CIRCLE -> CircleCommand(
                env,
                parentCommand(commands, current, env),
                current
            )

            SvgTagName.DEFS -> DefsCommand(
                env,
                mutableListOf(),
                parentCommand(commands, current, env),
                current
            )

            SvgTagName.ELLIPSE -> EllipseCommand(
                env,
                parentCommand(commands, current, env),
                current
            )

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

            SvgTagName.SCRIPT -> ScriptCommand(
                env,
                parentCommand(commands, current, env),
                current
            )

            SvgTagName.STOP -> StopCommand(
                env,
                parentCommand(commands, current, env),
                current
            )

            SvgTagName.SVG -> RootCommand(
                env,
                mutableListOf(),
                parentCommand(commands, current, env),
                current,
                mutableListOf()
            )

            SvgTagName.TEXT -> TextCommand(
                env,
                mutableListOf(),
                parentCommand(commands, current, env),
                current
            )

            else -> UnknownCommand(
                env
            )
        }
    }
    private fun parentCommand(commands: Map<Element, RenderCommand>, current: Element, env: RenderEnv): RenderCommand {
        val parent = current.parent() ?: return UnknownCommand(
            env
        )
        return commands.getOrElse(parent) {
            UnknownCommand(
                env
            )
        }
    }
}