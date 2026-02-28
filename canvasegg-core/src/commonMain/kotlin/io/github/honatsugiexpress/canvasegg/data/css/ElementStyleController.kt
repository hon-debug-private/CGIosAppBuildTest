package io.github.honatsugiexpress.canvasegg.data.css

import com.fleeksoft.ksoup.nodes.Attribute
import com.fleeksoft.ksoup.nodes.Element
import io.github.honatsugiexpress.cssparser.antlr.CssLexer
import io.github.honatsugiexpress.cssparser.antlr.CssParser
import io.github.honatsugiexpress.cssparser.antlr.CssStyleVisitor
import io.github.honatsugiexpress.cssparser.data.css.fontface.FontFaceDeclaration
import io.github.honatsugiexpress.cssparser.controller.CssStyleController
import io.github.honatsugiexpress.cssparser.data.css.CssRuleset
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream

class ElementStyleController(private val element: Element): CssStyleController {
    private val internalStyles: MutableMap<String, String> = mutableMapOf()
    val styles: Map<String, String> = internalStyles
    fun parseCss(content: String) {
        val lexer = CssLexer(CharStreams.fromString(content))
        val parser = CssParser(CommonTokenStream(lexer))
        val visitor = CssStyleVisitor(this)
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

    override fun addRuleset(ruleset: CssRuleset) {
        val match = ruleset.selectors.any { selectors ->
            selectors.all { part ->
                part.equalFeatures(ElementSelectorProvider(element))
            }
        }
        if (match) {
            internalStyles += ruleset.values
        }
    }

    override fun addFontFace(declaration: FontFaceDeclaration) = Unit
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