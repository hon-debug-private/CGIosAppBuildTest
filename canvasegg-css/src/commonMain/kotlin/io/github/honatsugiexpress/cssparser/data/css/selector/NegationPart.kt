package io.github.honatsugiexpress.cssparser.data.css.selector

import com.fleeksoft.ksoup.nodes.Element
import io.github.honatsugiexpress.cssparser.controller.CssSelectorProvider

class NegationPart(private val parts: List<SelectorPart>): SelectorPart() {
    override fun equalFeatures(provider: CssSelectorProvider): Boolean {
        return parts.none { part ->
            part.equalFeatures(provider)
        }
    }
    companion object {
        val Empty =
            NegationPart(
                emptyList()
            )
    }
}