package io.github.honatsugiexpress.cssparser.data.css.paint.generator

import io.github.honatsugiexpress.cssparser.data.css.paint.CssPaint
import io.github.honatsugiexpress.cssparser.data.css.paint.CssHexColor
import io.github.honatsugiexpress.cssparser.data.css.paint.CssRgbColor
import io.github.honatsugiexpress.cssparser.data.css.number.toFloat

fun CssPaint.toCssString(): String {
    return CssPaintStringGenerator().generate(this)
}

class CssPaintStringGenerator {
    fun generate(paint: CssPaint): String = buildString {
        when (paint) {
            is CssHexColor -> append("#${paint.hexValue}")
            is CssRgbColor -> generateRgb(paint)
            else -> append("none")
        }
    }
    private fun StringBuilder.generateRgb(color: CssRgbColor) {
        append("rgb(")
        append(color.red.toFloat(255f))
        appendSpace()
        append(color.green.toFloat(255f))
        appendSpace()
        append(color.blue.toFloat(255f))
        val alpha = color.alpha.toFloat()
        if (alpha != 1f) {
            append(" / ")
            append(color.alpha.toFloat(255f))
        }
        append(")")
    }
}

private fun StringBuilder.appendSpace() = append(" ")