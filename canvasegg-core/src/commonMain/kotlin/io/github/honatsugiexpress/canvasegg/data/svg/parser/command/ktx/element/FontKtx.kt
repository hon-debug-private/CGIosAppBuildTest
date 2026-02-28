package io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element

import androidx.compose.ui.text.font.FontWeight
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ElementCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.RenderCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.TextCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.TextContentCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.TextNodeContent
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.fontWeight
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.parentList
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgLength
import io.github.honatsugiexpress.canvasegg.data.svg.type.font.stringToFontWeight

val ElementCommand.fontSize
    get() = styleData.attrOrStyleOrNull("font-size")?.let {
        SvgLength(it)
    }



val TextCommand.fontWeight
    get() = stringToFontWeight(
        styleData.attrOrStyleOrNull("font-weight") ?: "400",
        this
    )

val TextContentCommand.fontWeight
    get() = when (this) {
        is TextNodeContent -> (parent as? TextCommand)?.fontWeight
            ?: FontWeight.W400
    }

val TextCommand.fontFamily
    get() = styleData
        .attrOrStyleOrNull("font-family")
        ?.split(",")
        ?.map { it.trim() } ?: emptyList()