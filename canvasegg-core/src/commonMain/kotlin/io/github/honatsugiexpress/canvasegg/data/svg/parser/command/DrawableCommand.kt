package io.github.honatsugiexpress.canvasegg.data.svg.parser.command

import androidx.compose.ui.graphics.drawscope.DrawScope

sealed interface DrawableCommand: PathCommand {
    fun draw(drawScope: DrawScope)
}