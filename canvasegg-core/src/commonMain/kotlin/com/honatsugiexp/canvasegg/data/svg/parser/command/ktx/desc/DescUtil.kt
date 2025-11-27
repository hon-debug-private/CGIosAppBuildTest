@file:Suppress("NOTHING_TO_INLINE")

package com.honatsugiexp.canvasegg.data.svg.parser.command.ktx.desc

import com.honatsugiexp.canvasegg.data.svg.parser.command.HasDescCommand
import com.honatsugiexp.canvasegg.data.svg.type.SvgTagName
import com.honatsugiexp.canvasegg.data.svg.type.ktx.svgTagName

inline val HasDescCommand.title: String?
    get() = element.firstNotNullOfOrNull {
        if (it.svgTagName() == SvgTagName.TITLE) {
            it.text()
        } else null
    }