package io.github.honatsugiexpress.canvasegg.data.svg.parser.command

import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgBoundingBox
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgLength

sealed interface ViewportCommand: ElementCommand {
    fun boundingBox(): SvgBoundingBox
    val fontSize: SvgLength
}

fun RenderCommand.viewport(): ViewportCommand {
    if (this is ViewportCommand) return this
    var current: RenderCommand? = this
    while (current?.parent != null) {
        if (current is ViewportCommand) return current
        current = current.parent
    }
    error("No element with a viewport found")
}