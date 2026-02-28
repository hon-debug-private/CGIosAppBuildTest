package io.github.honatsugiexpress.canvasegg.data.svg.parser.command

@Suppress("UNCHECKED_CAST")
open class HasSealedChildCommand<T>(
    override val env: RenderEnv,
    sealedChildren: MutableList<T>,
    override val parent: RenderCommand?
): HasChildCommand(env, sealedChildren.filterIsInstance<RenderCommand>().toMutableList(), parent) {
    final override val children: MutableList<RenderCommand>
        get() = super.children
}