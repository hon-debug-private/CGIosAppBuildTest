package io.github.honatsugiexpress.canvasegg.data.svg.parser.command

import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.nodes.Element
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.root

sealed class RenderCommand(
    open val env: RenderEnv,
    open val parent: RenderCommand?
) {
    companion object {
        fun parseDocument(document: Document, env: RenderEnv): List<RenderCommand> {
            val elemStack = ArrayDeque<Element>()
            elemStack.add(document)
            val commands = mutableMapOf<Element, RenderCommand>()
            val resolver =
                RenderCommandResolver(
                    commands,
                    env
                )
            while (elemStack.isNotEmpty()) {
                val current = elemStack.removeFirst()
                val command = resolver.resolve(current)
                val parent = parentCommand(commands, current, env)
                if (parent is HasChildCommand) {
                    parent.children.add(command)
                }
                if (command is DefCommand) {
                    command.root().defs += command
                    if (command is ScriptCommand) {
                        command.runScript()
                    }
                }
                commands[current] = command
                for (elem in current.children()) {
                    elemStack.add(elem)
                }
            }
            return commands.values.toList()
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
}

fun RenderCommand.parentList(): List<RenderCommand> {
    val stack = ArrayDeque<RenderCommand>()
    var current: RenderCommand? = parent
    while (current?.parent != null) {
        stack.addFirst(current)
        current = current.parent
    }
    return stack
}
fun RenderCommand.parentListWithSelf(): List<RenderCommand> {
    val stack = ArrayDeque<RenderCommand>()
    var current: RenderCommand? = this
    while (current?.parent != null) {
        stack.addFirst(current)
        current = current.parent
    }
    return stack
}

fun RenderCommand.root(): RootCommand {
    if (this is RootCommand) return this
    var current: RenderCommand? = this
    while (current?.parent != null) {
        if (current is RootCommand) return current
        current = current.parent
    }
    error("No a root element found")
}