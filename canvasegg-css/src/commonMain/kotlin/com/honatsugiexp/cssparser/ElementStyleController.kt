package com.honatsugiexp.cssparser

import com.fleeksoft.ksoup.nodes.Attribute
import com.fleeksoft.ksoup.nodes.Element
import com.honatsugiexp.cssparser.antlr.Css3Lexer
import com.honatsugiexp.cssparser.antlr.Css3Parser
import com.honatsugiexp.cssparser.antlr.NestedSelectorParts
import com.honatsugiexp.cssparser.antlr.StyleToElementVisitor
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream

class ElementStyleController(private val element: Element) {
    private val internalStyles: MutableMap<String, String> = mutableMapOf()
    val styles: Map<String, String> = internalStyles
    fun parseCss(content: String) {
        val lexer = Css3Lexer(CharStreams.fromString(content))
        val parser = Css3Parser(CommonTokenStream(lexer))
        val visitor = StyleToElementVisitor(this)
        visitor.visitStylesheet(parser.stylesheet())
    }
    fun parseDocumentStyles() {
        val styles = element.root().getElementsByTag("style").asSequence()
        styles
            .map { style ->
                style.text()
            }
            .forEach { content ->
                parseCss(content)
            }
    }
    fun addStyles(map: Map<String, String>) {
        internalStyles += map
    }
    fun equalSelector(selectorParts: NestedSelectorParts): Boolean {
        return selectorParts.any { selectors ->
            selectors.all { part ->
                part.equalFeatures(element)
            }
        }
    }
    fun attrOrStyleOrNull(attrName: String): String? {
        return if (styles.containsKey(attrName)) {
            styles.getValue(attrName)
        } else if (element.hasAttr(attrName)) {
            element.attr(attrName)
        } else null
    }
    fun attributes() = styles.map { (key, value) ->
        Attribute(key, value)
    } + element.attributes()
}