package com.honatsugiexp.canvasegg.data.svg.parser.command

sealed class GradientCommand(
    override val env: RenderEnv,
    sealedChildren: MutableList<StopCommand>,
    override val parent: RenderCommand?
): HasSealedChildCommand<StopCommand>(env, sealedChildren, parent) {
    val stops = sealedChildren
}