package io.github.honatsugiexpress.cssparser.data.css.selector

import com.fleeksoft.ksoup.nodes.Element
import io.github.honatsugiexpress.cssparser.controller.CssSelectorProvider

sealed class SelectorPart {
    open fun equalFeatures(provider: CssSelectorProvider) = false
}