package com.honatsugiexp.cssparser.antlr.selector

import com.fleeksoft.ksoup.nodes.Element

internal data class ClassPart(val name: String): SelectorPart() {
    override fun equalFeatures(otherElement: Element): Boolean {
        return otherElement.classNames().contains(name)
    }
}