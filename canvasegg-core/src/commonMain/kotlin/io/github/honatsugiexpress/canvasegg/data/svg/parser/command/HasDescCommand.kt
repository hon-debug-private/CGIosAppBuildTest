package io.github.honatsugiexpress.canvasegg.data.svg.parser.command

import com.fleeksoft.ksoup.nodes.Element

sealed interface HasDescCommand {
    val element: Element
}