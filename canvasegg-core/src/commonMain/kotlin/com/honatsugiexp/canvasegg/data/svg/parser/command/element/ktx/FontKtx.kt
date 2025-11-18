package com.honatsugiexp.canvasegg.data.svg.parser.command.element.ktx

import com.honatsugiexp.canvasegg.data.svg.parser.command.ElementCommand
import com.honatsugiexp.canvasegg.data.svg.type.SvgLength

fun ElementCommand.fontSize() = controller.attrOrStyleOrNull("font-size")?.let {
    SvgLength(it)
}