package com.honatsugiexp.canvasegg.data.svg.parser.command

import androidx.compose.ui.graphics.Path

interface PathCommand {
    fun path(): Path
}

@Suppress("NOTHING_TO_INLINE")
inline fun PathCommand.boundingBox() = path().getBounds()