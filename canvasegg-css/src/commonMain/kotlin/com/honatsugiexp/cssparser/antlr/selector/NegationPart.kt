package com.honatsugiexp.cssparser.antlr.selector

import com.fleeksoft.ksoup.nodes.Element

internal class NegationPart(private val parts: List<SelectorPart>): SelectorPart() {
    override fun equalFeatures(otherElement: Element): Boolean {
        return parts.none { part ->
            part.equalFeatures(otherElement)
        }
    }
    companion object {
        val Empty = NegationPart(emptyList())
    }
}