package com.honatsugiexp.canvasegg.data.svg.parser.command

import androidx.compose.ui.graphics.Path

interface HasChildPathCommand {
    fun childrenPath(): Path
}