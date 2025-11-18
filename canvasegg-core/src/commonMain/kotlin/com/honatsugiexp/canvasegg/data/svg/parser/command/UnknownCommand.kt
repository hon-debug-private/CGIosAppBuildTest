package com.honatsugiexp.canvasegg.data.svg.parser.command

data class UnknownCommand(override val env: RenderEnv): RenderCommand(env) {
    override fun toString(): String {
        return "UnknownCommand"
    }
}