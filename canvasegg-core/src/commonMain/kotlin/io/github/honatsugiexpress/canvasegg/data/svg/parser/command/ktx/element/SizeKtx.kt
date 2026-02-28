@file:Suppress("NOTHING_TO_INLINE")

package io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element

import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ElementCommand
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgLength

inline val ElementCommand.width
    get() = styleData.attrOrStyleOrNull("width")?.let {
        SvgLength(it)
    }

inline val ElementCommand.height
    get() = styleData.attrOrStyleOrNull("height")?.let {
        SvgLength(it)
    }