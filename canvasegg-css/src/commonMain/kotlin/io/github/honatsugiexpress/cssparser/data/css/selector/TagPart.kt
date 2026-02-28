package io.github.honatsugiexpress.cssparser.data.css.selector

import com.fleeksoft.ksoup.nodes.Element
import io.github.honatsugiexpress.cssparser.controller.CssSelectorProvider

data class TagPart(val name: NamespacedName): SelectorPart() {
    override fun equalFeatures(provider: CssSelectorProvider): Boolean {
        return name.name == provider.tagName()
    }
}