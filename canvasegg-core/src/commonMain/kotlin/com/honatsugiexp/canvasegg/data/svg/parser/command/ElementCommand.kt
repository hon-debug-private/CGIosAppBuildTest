package com.honatsugiexp.canvasegg.data.svg.parser.command

import com.fleeksoft.ksoup.nodes.Element
import com.honatsugiexp.cssparser.ElementStyleController

interface ElementCommand {
    val controller: ElementStyleController
    val element: Element
    val env: RenderEnv
}

internal fun ElementCommand.controller() = ElementStyleController(element).apply {
    parseDocumentStyles()
}