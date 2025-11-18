package com.honatsugiexp.cssparser.antlr.selector

import com.fleeksoft.ksoup.nodes.Element

sealed class SelectorPart {
    open fun equalFeatures(otherElement: Element) = false
}