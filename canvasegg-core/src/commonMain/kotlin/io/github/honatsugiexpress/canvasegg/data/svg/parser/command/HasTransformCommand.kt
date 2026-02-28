package io.github.honatsugiexpress.canvasegg.data.svg.parser.command

import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.transform.TransformCommand

sealed interface HasTransformCommand {
    val parent: RenderCommand?
    fun transformCommands(): List<TransformCommand>
}