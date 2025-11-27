package com.honatsugiexp.canvasegg.data.svg.parser.command.ktx.element

import com.honatsugiexp.canvasegg.data.svg.parser.command.ElementCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.RenderCommand
import com.honatsugiexp.canvasegg.data.svg.type.SvgLength

val ElementCommand.lenEnv
    get() = SvgLength.Env.fromCommand(this as RenderCommand, env.density)