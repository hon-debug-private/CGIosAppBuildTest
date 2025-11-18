package com.honatsugiexp.canvasegg.data.svg.parser.command

import com.honatsugiexp.canvasegg.data.svg.parser.command.transform.TransformCommand

interface HasTransformCommand {
    val parent: RenderCommand?
    fun transformCommands(): List<TransformCommand>
}