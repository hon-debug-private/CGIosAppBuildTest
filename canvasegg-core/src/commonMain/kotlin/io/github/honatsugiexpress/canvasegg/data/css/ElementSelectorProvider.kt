package io.github.honatsugiexpress.canvasegg.data.css

import com.fleeksoft.ksoup.nodes.Element
import io.github.honatsugiexpress.cssparser.controller.CssSelectorProvider

class ElementSelectorProvider(private val element: Element): CssSelectorProvider {
    override fun attr(name: String): String = element.attr(name)

    override fun classNames(): Collection<String> = element.classNames()

    override fun id(): String = element.id()

    override fun tagName(): String = element.tagName()
}