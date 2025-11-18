package com.honatsugiexp.canvasegg.data.svg.parser.command.transform

import androidx.compose.ui.graphics.Path

data class ClipPathTransformCommand(val path: Path): TransformCommand()