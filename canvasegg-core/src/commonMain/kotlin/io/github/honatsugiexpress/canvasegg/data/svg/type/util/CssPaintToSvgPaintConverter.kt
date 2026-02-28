package io.github.honatsugiexpress.canvasegg.data.svg.type.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.RenderEnv
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgPaint
import io.github.honatsugiexpress.cssparser.antlr.CssPaintVisitor
import io.github.honatsugiexpress.cssparser.data.css.paint.CssRgbColor
import io.github.honatsugiexpress.cssparser.data.css.paint.generator.toCssString
import io.github.honatsugiexpress.cssparser.data.css.number.toFloat
import io.github.honatsugiexpress.cssparser.data.css.paint.CssHexColor
import io.github.honatsugiexpress.cssparser.data.css.paint.CssHslColor
import io.github.honatsugiexpress.cssparser.data.css.paint.CssHwbColor
import io.github.honatsugiexpress.cssparser.data.css.paint.CssLabColor
import io.github.honatsugiexpress.cssparser.data.css.paint.CssLchColor
import io.github.honatsugiexpress.cssparser.data.css.paint.CssLightDark
import io.github.honatsugiexpress.cssparser.data.css.paint.CssOklabColor
import io.github.honatsugiexpress.cssparser.data.css.paint.CssOklchColor
import io.github.honatsugiexpress.cssparser.data.css.paint.CssPaint
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class CssPaintToSvgPaintConverter {
    private val visitor = CssPaintVisitor()
    fun parse(value: String, env: RenderEnv): SvgPaint {
        val paint = visitor.parseColor(value)
        return if (paint == null) {
            SvgPaint.None
        } else {
            parse(paint, env)
        }
    }
    private fun parse(color: CssPaint, env: RenderEnv): SvgPaint {
        return when (color) {
            is CssHexColor -> parseHex(color)
            is CssRgbColor -> parseRgb(color)
            is CssHslColor -> parseHsl(color)
            is CssHwbColor -> parseHwb(color)
            is CssLabColor -> parseLab(color)
            is CssOklabColor -> parseOklab(color)
            is CssLchColor -> parseLch(color)
            is CssOklchColor -> parseOklch(color)
            is CssLightDark -> parseLightDark(color, env)
            else -> SvgPaint.None
        }
    }
    private fun parseHex(color: CssHexColor): SvgPaint {
        val hex = color.hexValue
        val composeColor = when (color.hexValue.length) {
            3 -> {
                val r = hex[0].toString().repeat(2)
                val g = hex[1].toString().repeat(2)
                val b = hex[2].toString().repeat(2)
                Color("$r$g$b".toLong())
            }
            4 -> {
                val a = hex[0].toString().repeat(2)
                val r = hex[1].toString().repeat(2)
                val g = hex[2].toString().repeat(2)
                val b = hex[3].toString().repeat(2)
                Color("$a$r$g$b".toLong(16))
            }
            6 -> {
                Color("FF$hex".toLong(16))
            }
            8 -> {
                Color(hex.toLong(16))
            }
            else -> throw IllegalArgumentException("HEX string in unsupported format: #$hex")
        }
        return SvgPaint.Color(composeColor, color)
    }
    private fun parseRgb(color: CssRgbColor): SvgPaint {
        return SvgPaint.Color(
            Color(
                color.red.toFloat(255f) / 255f,
                color.green.toFloat(255f) / 255f,
                color.blue.toFloat(255f) / 255f,
                color.alpha.toFloat()
            ),
            color
        )
    }
    private fun parseHsl(color: CssHslColor): SvgPaint {
        val hue = color.hue.toDegreesValue()
        val saturation = color.saturation.toFloat()
        val lightness = color.lightness.toFloat()
        val c = (1 - abs(2 * lightness - 1)) * saturation
        val x = c * (1 - abs((hue / 60) % 2 - 1))
        val m = lightness - c / 2

        val (rp, gp, bp) = when {
            hue < 60  -> Triple(c, x, 0f)
            hue < 120 -> Triple(x, c, 0f)
            hue < 180 -> Triple(0f, c, x)
            hue < 240 -> Triple(0f, x, c)
            hue < 300 -> Triple(x, 0f, c)
            else    -> Triple(c, 0f, x)
        }
        return SvgPaint.Color(
            Color(
                (rp + m) * 255,
                (gp + m) * 255,
                (bp + m) * 255,
                color.alpha.toFloat()
            ),
            color
        )
    }
    private fun parseHwb(color: CssHwbColor): SvgPaint {
        var white = color.whiteness.toFloat()
        var black = color.blackness.toFloat()
        val hue = color.hue.toDegreesValue()
        val ratio = white + black
        if (ratio > 1f) {
            white /= ratio
            black /= ratio
        }

        val c = 1f - white - black
        val x = c * (1f - abs((hue / 60f) % 2f - 1f))

        val (rp, gp, bp) = when {
            hue < 60  -> Triple(c, x, 0f)
            hue < 120 -> Triple(x, c, 0f)
            hue < 180 -> Triple(0f, c, x)
            hue < 240 -> Triple(0f, x, c)
            hue < 300 -> Triple(x, 0f, c)
            else    -> Triple(c, 0f, x)
        }

        return SvgPaint.Color(
            Color(
                (rp + white) * 255f,
                (gp + white) * 255f,
                (bp + white) * 255f,
                color.alpha.toFloat()
            ),
            color
        )
    }
    private fun parseLab(color: CssLabColor): SvgPaint {
        return SvgPaint.Color(
            Color(
                color.lightness.toFloat(100f),
                color.aAxis.toFloat(125f),
                color.bAxis.toFloat(125f),
                color.alpha.toFloat(),
                ColorSpaces.CieLab
            ),
            color
        )
    }
    private fun parseOklab(color: CssOklabColor): SvgPaint {
        return SvgPaint.Color(
            Color(
                color.lightness.toFloat(),
                color.aAxis.toFloat(0.4f),
                color.bAxis.toFloat(0.4f),
                color.alpha.toFloat(),
                ColorSpaces.Oklab
            ),
            color
        )
    }
    private fun parseLch(color: CssLchColor): SvgPaint {
        val hueRad = (color.hue.toDegreesValue() / 180.0 * PI).toFloat()
        val chroma = color.chroma.toFloat(150f)

        val aAxis = chroma * cos(hueRad)
        val bAxis = chroma * sin(hueRad)
        return SvgPaint.Color(
            Color(
                color.lightness.toFloat(100f),
                aAxis,
                bAxis,
                color.alpha.toFloat(),
                ColorSpaces.CieLab
            ),
            color
        )
    }
    private fun parseOklch(color: CssOklchColor): SvgPaint {
        val hueRad = (color.hue.toDegreesValue() / 180.0 * PI).toFloat()
        val chroma = color.chroma.toFloat(150f)

        val aAxis = chroma * cos(hueRad)
        val bAxis = chroma * sin(hueRad)
        return SvgPaint.Color(
            Color(
                color.lightness.toFloat(100f),
                aAxis,
                bAxis,
                color.alpha.toFloat(),
                ColorSpaces.Oklab
            ),
            color
        )
    }

    private fun parseLightDark(color: CssLightDark, env: RenderEnv): SvgPaint {
        return if (env.isDarkMode) {
            parse(color.light, env)
        } else {
            parse(color.dark, env)
        }
    }
}