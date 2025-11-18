package com.honatsugiexp.canvasegg.data.svg.parser.command

@Suppress("UNCHECKED_CAST")
open class HasSealedChildCommand<T: RenderCommand>(
    override val env: RenderEnv,
    sealedChildren: MutableList<T>,
    override val parent: RenderCommand?
): HasChildCommand(env, sealedChildren as MutableList<RenderCommand>, parent) {
    final override val children: MutableList<RenderCommand>
        get() = super.children
}