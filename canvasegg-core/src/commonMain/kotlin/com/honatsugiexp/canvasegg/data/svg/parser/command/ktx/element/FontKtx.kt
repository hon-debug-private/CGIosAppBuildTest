package com.honatsugiexp.canvasegg.data.svg.parser.command.ktx.element

import com.honatsugiexp.canvasegg.data.svg.parser.command.ElementCommand
import com.honatsugiexp.canvasegg.data.svg.type.SvgLength

val ElementCommand.fontSize
    get() = controller.attrOrStyleOrNull("font-size")?.let {
        SvgLength(it)
    }