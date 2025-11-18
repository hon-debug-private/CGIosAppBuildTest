package com.honatsugiexp.canvasegg.data.svg.parser.command

sealed class HasParentCommand(
    override val env: RenderEnv,
    open val parent: RenderCommand?
): RenderCommand(env) {

}

@Suppress("NOTHING_TO_INLINE")
inline fun HasParentCommand.parentList(): List<RenderCommand> {
    val stack = ArrayDeque<RenderCommand>()
    var current: RenderCommand? = parent
    while (current is HasParentCommand) {
        stack.addFirst(current)
        current = current.parent
    }
    return stack
}

@Suppress("NOTHING_TO_INLINE")
inline fun HasParentCommand.parentListWithSelf(): List<RenderCommand> {
    val stack = ArrayDeque<RenderCommand>()
    var current: RenderCommand? = this
    while (current is HasParentCommand) {
        stack.addFirst(current)
        current = current.parent
    }
    return stack
}

@Suppress("NOTHING_TO_INLINE")
inline fun HasParentCommand.root(): RootCommand? {
    var current: RenderCommand? = this
    while (current is HasParentCommand) {
        current = current.parent
    }
    return current as? RootCommand
}