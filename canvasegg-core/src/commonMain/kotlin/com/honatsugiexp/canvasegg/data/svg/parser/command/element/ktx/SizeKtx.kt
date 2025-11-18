@file:Suppress("NOTHING_TO_INLINE")

package com.honatsugiexp.canvasegg.data.svg.parser.command.element.ktx

import com.honatsugiexp.canvasegg.data.svg.parser.command.ElementCommand
import com.honatsugiexp.canvasegg.data.svg.type.SvgLength

inline fun ElementCommand.width() = controller.attrOrStyleOrNull("width")?.let {
    SvgLength(it)
}

inline fun ElementCommand.height() = controller.attrOrStyleOrNull("height")?.let {
    SvgLength(it)
}