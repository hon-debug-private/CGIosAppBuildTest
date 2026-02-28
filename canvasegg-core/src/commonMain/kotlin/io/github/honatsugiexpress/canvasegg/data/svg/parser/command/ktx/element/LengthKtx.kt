package io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element

import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ElementCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.RenderCommand
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgLength
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgPercentResolver

inline val ElementCommand.xLenEnv
    get() = SvgLength.Env.fromCommand(this as RenderCommand, env.density, SvgLength.Env.RefDirection.X)

inline val ElementCommand.yLenEnv
    get() = SvgLength.Env.fromCommand(this as RenderCommand, env.density, SvgLength.Env.RefDirection.Y)

inline val ElementCommand.lenEnv
    get() = SvgLength.Env.fromCommand(this as RenderCommand, env.density, SvgLength.Env.RefDirection.None)

fun ElementCommand.lenEnvWithResolver(resolver: SvgPercentResolver) =
    SvgLength.Env.fromCommand(
        this as RenderCommand,
        env.density,
        SvgLength.Env.RefDirection.None,
        resolver
    )