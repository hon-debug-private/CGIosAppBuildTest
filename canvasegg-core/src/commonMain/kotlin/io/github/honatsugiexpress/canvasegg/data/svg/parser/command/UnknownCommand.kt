package io.github.honatsugiexpress.canvasegg.data.svg.parser.command

data class UnknownCommand(
    override val env: RenderEnv
): RenderCommand(env, null) {
    override fun toString(): String {
        return "UnknownCommand"
    }
}