package io.github.honatsugiexpress.canvasegg.data.svg.parser.command

import androidx.compose.ui.graphics.Path

sealed class HasChildCommand(
    override val env: RenderEnv,
    open val children: MutableList<RenderCommand>,
    override val parent: RenderCommand?
): RenderCommand(env, parent)

@Suppress("NOTHING_TO_INLINE")
inline fun HasChildCommand.childrenList(): List<RenderCommand> {
    val stack = ArrayDeque<RenderCommand>()
    val currentStack = ArrayDeque<RenderCommand>()
    currentStack.add(this)
    while (currentStack.isNotEmpty()) {
        val current = currentStack.removeFirst()
        if (current is HasChildCommand) {
            for (elem in current.children) {
                currentStack.add(elem)
            }
        }
    }
    return stack
}