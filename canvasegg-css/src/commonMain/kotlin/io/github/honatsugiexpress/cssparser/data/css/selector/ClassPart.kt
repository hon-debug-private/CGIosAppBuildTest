package io.github.honatsugiexpress.cssparser.data.css.selector

import com.fleeksoft.ksoup.nodes.Element
import io.github.honatsugiexpress.cssparser.controller.CssSelectorProvider

data class ClassPart(val name: String): SelectorPart() {
    override fun equalFeatures(provider: CssSelectorProvider): Boolean {
        return provider.classNames().contains(name)
    }
}