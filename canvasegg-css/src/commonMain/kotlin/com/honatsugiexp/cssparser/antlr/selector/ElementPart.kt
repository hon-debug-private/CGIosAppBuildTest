package com.honatsugiexp.cssparser.antlr.selector

import com.fleeksoft.ksoup.nodes.Element

internal data class ElementPart(val name: NamespacedName): SelectorPart() {
    override fun equalFeatures(otherElement: Element): Boolean {
        return name.name == otherElement.tagName()
    }
}