package io.github.honatsugiexpress.canvasegg.data.svg.parser.command

import com.fleeksoft.ksoup.nodes.Element
import io.github.honatsugiexpress.canvasegg.data.css.ElementStyleData

sealed interface ElementCommand {
    val styleData: ElementStyleData
    val element: Element
    val env: RenderEnv
}

fun ElementCommand.styleData() = env.documentStyleController.data(element)