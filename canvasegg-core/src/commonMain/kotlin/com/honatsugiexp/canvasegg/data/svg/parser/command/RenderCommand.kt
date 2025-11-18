package com.honatsugiexp.canvasegg.data.svg.parser.command

import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.nodes.Element
import com.honatsugiexp.canvasegg.data.svg.type.SvgTagName
import com.honatsugiexp.canvasegg.data.svg.type.ktx.svgTagName

sealed class RenderCommand(
    open val env: RenderEnv
) {
    companion object {
        fun parseDocument(document: Document, env: RenderEnv): List<RenderCommand> {
            val elemStack = ArrayDeque<Element>()
            elemStack.add(document)
            val commands = mutableMapOf<Element, RenderCommand>()
            val defsElements = document.getElementsByTag("defs")
            val defsCommands = defsElements.map {
                DefsCommand(env, mutableListOf(), parentCommand(commands, it, env), it)
            }.toMutableList()
            val resolver = RenderCommandResolver(commands, env)
            while (elemStack.isNotEmpty()) {
                val current = elemStack.removeFirst()
                val command = resolver.resolve(current)
                val parent = parentCommand(commands, current, env)
                if (parent is HasChildCommand) {
                    parent.children.add(command)
                }
                commands[current] = command
                for (elem in current.children()) {
                    elemStack.add(elem)
                }
            }
            return commands.values.toList()
        }
        private fun parentCommand(commands: Map<Element, RenderCommand>, current: Element, env: RenderEnv): RenderCommand {
            val parent = current.parent() ?: return UnknownCommand(env)
            return commands.getOrElse(parent) {
                UnknownCommand(env)
            }
        }
    }
}