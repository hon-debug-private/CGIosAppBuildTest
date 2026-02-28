@file:Suppress("NOTHING_TO_INLINE")

package io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.desc

import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.HasDescCommand
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgTagName
import io.github.honatsugiexpress.canvasegg.data.svg.type.util.ktx.svgTagName

inline val HasDescCommand.title: String?
    get() = element.firstNotNullOfOrNull {
        if (it.svgTagName() == SvgTagName.TITLE) {
            it.text()
        } else null
    }