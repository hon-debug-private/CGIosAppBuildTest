package com.honatsugiexp.canvasegg.data.svg.parser.command

import androidx.compose.ui.graphics.drawscope.DrawScope

interface DrawableCommand: PathCommand {
    fun draw(drawScope: DrawScope)
}