@file:Suppress("NOTHING_TO_INLINE")

package com.honatsugiexp.canvasegg.data.svg.parser.command.ktx.element

import com.honatsugiexp.canvasegg.data.svg.parser.command.ElementCommand
import com.honatsugiexp.canvasegg.data.svg.type.SvgLength

inline val ElementCommand.width
    get() = controller.attrOrStyleOrNull("width")?.let {
        SvgLength(it)
    }

inline val ElementCommand.height
    get() = controller.attrOrStyleOrNull("height")?.let {
        SvgLength(it)
    }