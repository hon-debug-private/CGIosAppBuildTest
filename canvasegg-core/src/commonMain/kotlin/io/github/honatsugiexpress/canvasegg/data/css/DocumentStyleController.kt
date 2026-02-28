package io.github.honatsugiexpress.canvasegg.data.css

import com.fleeksoft.ksoup.nodes.Attribute
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.nodes.Element
import io.github.honatsugiexpress.canvasegg.common.log.CanvasEggLogger
import io.github.honatsugiexpress.cssparser.antlr.CssLexer
import io.github.honatsugiexpress.cssparser.antlr.CssParser
import io.github.honatsugiexpress.cssparser.antlr.CssStyleVisitor
import io.github.honatsugiexpress.cssparser.data.css.fontface.FontFaceDeclaration
import io.github.honatsugiexpress.cssparser.controller.CssStyleController
import io.github.honatsugiexpress.cssparser.data.css.CssRuleset
import org.antlr.v4.kotlinruntime.BailErrorStrategy
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream
import org.antlr.v4.kotlinruntime.DefaultErrorStrategy
import org.antlr.v4.kotlinruntime.NoViableAltException
import org.antlr.v4.kotlinruntime.atn.PredictionMode
import org.antlr.v4.kotlinruntime.misc.ParseCancellationException
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.plus

class DocumentStyleController(private val document: Document): CssStyleController {
    private val managedElements: MutableList<Element> = mutableListOf()
    private val styles: MutableMap<Element, MutableMap<String, String>> = mutableMapOf()
    private val internalFontFaces: MutableList<FontFaceDeclaration> = mutableListOf()
    val fontFaces: List<FontFaceDeclaration>
        get() = internalFontFaces
    fun parseDocumentStyles() {
        val styles = document.getElementsByTag("style").asSequence()
        val lexer = CssLexer(CharStreams.fromString(""))
        val tokenStream = CommonTokenStream(lexer)
        val parser = CssParser(tokenStream)
        styles.forEach { style ->
            val content = style.text()
            if (content.isBlank()) return@forEach
            lexer.inputStream = CharStreams.fromString(content)
            tokenStream.tokenSource = lexer
            parser.tokenStream = tokenStream
            parser.interpreter.predictionMode = PredictionMode.SLL
            parser.errorHandler = BailErrorStrategy()
            try {
                val visitor = CssStyleVisitor(this)
                visitor.visitStylesheet(parser.stylesheet())
            } catch (_: ParseCancellationException) {
                tokenStream.seek(0)
                parser.reset()
                parser.interpreter.predictionMode = PredictionMode.LL
                parser.errorHandler = DefaultErrorStrategy()
                try {
                    val visitor = CssStyleVisitor(this)
                    visitor.visitStylesheet(parser.stylesheet())
                } catch (e: Exception) {
                    CanvasEggLogger.errorThrowable(e)
                }
            }
        }
    }
    override fun addRuleset(ruleset: CssRuleset) {
        val matched = managedElements.filter { elem ->
            ruleset.selectors.any { parts ->
                parts.all {
                    it.equalFeatures(ElementSelectorProvider(elem))
                }
            }
        }
        matched.forEach {
            styles[it]!! += ruleset.values
        }
    }

    override fun addFontFace(declaration: FontFaceDeclaration) {
        internalFontFaces += declaration
    }

    fun data(element: Element) = ElementStyleData(element, styles[element] ?: emptyMap())

    fun addManageElement(element: Element) {
        managedElements.add(element)
        styles[element] = mutableMapOf()
    }
}

class ElementStyleData(
    private val element: Element,
    private val styles: Map<String, String>
) {
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

