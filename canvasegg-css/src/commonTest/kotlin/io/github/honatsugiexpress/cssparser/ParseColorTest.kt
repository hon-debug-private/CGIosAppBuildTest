package io.github.honatsugiexpress.cssparser

import io.github.honatsugiexpress.cssparser.antlr.CssPaintVisitor
import kotlin.test.Test

class ParseColorTest {
    @Test
    fun parseCssColor1() {
        val visitor = CssPaintVisitor()
        println(visitor.parseColor("blue"))
        println(visitor.parseColor("#00FF0055"))
        println(visitor.parseColor("rgb(255 255 255 / 50%)"))
        println(visitor.parseColor("rgb(from white 0 g b)"))
        println(visitor.parseColor("lab(29.2345% 39.3825 20.0664)"))
        println(visitor.parseColor("hsl(20rad 75% 25%)"))
        println(visitor.parseColor("light-dark(oklch(40.1% 0.123 21.57), lch(29.2345% 44.2 27))"))
    }
}