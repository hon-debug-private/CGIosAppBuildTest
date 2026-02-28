package io.github.honatsugiexpress.canvasegg.data.svg.parser.command

sealed class GradientCommand(
    override val env: RenderEnv,
    val stops: MutableList<StopCommand>,
    override val parent: RenderCommand?
): HasSealedChildCommand<StopCommand>(env, stops, parent)