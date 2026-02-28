package io.github.honatsugiexpress.canvasegg.data.svg.parser.command

import androidx.compose.ui.text.TextMeasurer
import io.github.honatsugiexpress.canvasegg.data.css.DocumentStyleController
import io.github.honatsugiexpress.canvasegg.data.svg.parser.SvgCanvasParser
import io.github.honatsugiexpress.canvasegg.data.svg.parser.SvgColorSchema
import io.github.honatsugiexpress.canvasegg.data.svg.resolver.script.SvgScriptEngine
import io.github.honatsugiexpress.canvasegg.data.svg.resolver.uri.SvgUriResolver
import io.github.honatsugiexpress.cssparser.data.css.fontface.FontFaceDeclaration

data class RenderEnv(
    val parser: SvgCanvasParser,
    val density: Float,
    val textMeasurer: TextMeasurer,
    val documentStyleController: DocumentStyleController = DocumentStyleController(parser.document).apply {
        parseDocumentStyles()
    }
) {
    inline val uriResolver: List<SvgUriResolver>
        get() = parser.env.option.uriResolvers
    inline val scriptEngine: SvgScriptEngine?
        get() = parser.env.option.scriptEngine
    inline val fontFaces: List<FontFaceDeclaration>
        get() = documentStyleController.fontFaces
    inline val isDarkMode: Boolean
        get() = parser.env.isDarkMode
}