package io.github.honatsugiexpress.cssparser.controller

import io.github.honatsugiexpress.cssparser.data.css.fontface.FontFaceDeclaration
import io.github.honatsugiexpress.cssparser.data.css.CssRuleset

interface CssStyleController {
    fun addRuleset(ruleset: CssRuleset)
    fun addFontFace(declaration: FontFaceDeclaration)
}