package io.github.honatsugiexpress.cssparser

import io.github.honatsugiexpress.cssparser.data.css.paint.CssHexColor
import io.github.honatsugiexpress.cssparser.data.css.paint.CssRgbColor
import io.github.honatsugiexpress.cssparser.data.css.paint.generator.toCssString
import io.github.honatsugiexpress.cssparser.data.css.number.CssFloat
import kotlin.test.Test

class GenerateColorStringTest {
    @Test
    fun generateRgbColorString1() {
        val color = CssRgbColor(CssFloat(0f), CssFloat(0f), CssFloat(0f), CssFloat(0f))
        println(color.toCssString())
    }
    @Test
    fun generateHexColorString1() {
        val color = CssHexColor("005500FF")
        println(color.toCssString())
    }
}