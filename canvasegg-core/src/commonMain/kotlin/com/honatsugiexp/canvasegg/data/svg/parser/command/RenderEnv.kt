package com.honatsugiexp.canvasegg.data.svg.parser.command

import com.honatsugiexp.canvasegg.data.svg.parser.SvgCanvasParser

data class RenderEnv(
    val parser: SvgCanvasParser,
    val density: Float
)