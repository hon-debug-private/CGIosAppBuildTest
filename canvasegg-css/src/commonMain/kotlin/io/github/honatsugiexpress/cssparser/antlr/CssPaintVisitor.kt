package io.github.honatsugiexpress.cssparser.antlr

import io.github.honatsugiexpress.canvasegg.common.log.CanvasEggLogger
import io.github.honatsugiexpress.cssparser.data.css.CssAngle
import io.github.honatsugiexpress.cssparser.data.css.number.CssFloat
import io.github.honatsugiexpress.cssparser.data.css.number.CssPercentFloat
import io.github.honatsugiexpress.cssparser.data.css.paint.CssColorKeywords
import io.github.honatsugiexpress.cssparser.data.css.paint.CssFromColor
import io.github.honatsugiexpress.cssparser.data.css.paint.CssHexColor
import io.github.honatsugiexpress.cssparser.data.css.paint.CssHslColor
import io.github.honatsugiexpress.cssparser.data.css.paint.CssHwbColor
import io.github.honatsugiexpress.cssparser.data.css.paint.CssLabColor
import io.github.honatsugiexpress.cssparser.data.css.paint.CssLchColor
import io.github.honatsugiexpress.cssparser.data.css.paint.CssLightDark
import io.github.honatsugiexpress.cssparser.data.css.paint.CssNamedColor
import io.github.honatsugiexpress.cssparser.data.css.paint.CssOklabColor
import io.github.honatsugiexpress.cssparser.data.css.paint.CssOklchColor
import io.github.honatsugiexpress.cssparser.data.css.paint.CssPaint
import io.github.honatsugiexpress.cssparser.data.css.paint.CssRelativeComponent
import io.github.honatsugiexpress.cssparser.data.css.paint.CssRgbColor
import io.github.honatsugiexpress.cssparser.data.css.paint.UnknownCssColor
import io.github.honatsugiexpress.cssparser.util.percentOrValueToValue
import io.github.honatsugiexpress.cssparser.util.percentOrValueToValueRange
import org.antlr.v4.kotlinruntime.BailErrorStrategy
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream
import org.antlr.v4.kotlinruntime.DefaultErrorStrategy
import org.antlr.v4.kotlinruntime.atn.PredictionMode
import org.antlr.v4.kotlinruntime.misc.ParseCancellationException

class CssPaintVisitor: CssPaintParserBaseVisitor<Any>() {
    private val lexer = CssPaintLexer(CharStreams.fromString(""))
    private val tokenStream = CommonTokenStream(lexer)
    private val parser = CssPaintParser(tokenStream)
    override fun defaultResult() = Unit
    override fun visitPaint(ctx: CssPaintParser.PaintContext): CssPaint {
        println(ctx.text)
        return when {
            ctx.baseColor() != null -> visitBaseColor(ctx.baseColor()!!)
            ctx.lightDark() != null -> visitLightDark(ctx.lightDark()!!)
            else -> UnknownCssColor
        }
    }

    override fun visitBaseColor(ctx: CssPaintParser.BaseColorContext): CssPaint {
        return ctx.getChild(0)?.let { visit(it) as? CssPaint } ?: UnknownCssColor
    }

    override fun visitHexColor(ctx: CssPaintParser.HexColorContext): CssPaint {
        return CssHexColor(ctx.HASH_HEX().text.removePrefix("#"))
    }

    override fun visitAbsoluteRgbColor(ctx: CssPaintParser.AbsoluteRgbColorContext): CssPaint {
        val red = CssFloat(ctx.component(0)?.text)
        val green = CssFloat(ctx.component(1)?.text)
        val blue = CssFloat(ctx.component(2)?.text)
        val alpha = CssFloat(ctx.alpha()?.text, 1f)
        return CssRgbColor(red, green, blue, alpha)
    }

    override fun visitRelativeRgbColor(ctx: CssPaintParser.RelativeRgbColorContext): CssPaint {
        val originalColor = visitBaseColor(ctx.baseColor())
        val components = ctx.relativeComponent()
        val red = visitRelativeComponent(components[0])
        val green = visitRelativeComponent(components[1])
        val blue = visitRelativeComponent(components[2])
        val alpha = percentOrValueToValue(ctx.alpha()?.text, 1f)
        return CssFromColor(originalColor, listOf(red, green, blue), alpha)
    }

    override fun visitHslColor(ctx: CssPaintParser.HslColorContext): CssPaint {
        val hueValue = ctx.hue().NUMBER().text
        val hueUnit = ctx.hue().DEG()?.text ?: ""
        val hue = CssAngle(hueValue, hueUnit)
        val saturation =
            CssPercentFloat(ctx.saturation().percentComponent().percent().NUMBER().text)
        val lightness =
            CssPercentFloat(ctx.lightnessPercent().percentComponent().percent().NUMBER().text)
        val alpha = CssFloat(ctx.alpha()?.text, 1f)
        return CssHslColor(
            hue,
            saturation,
            lightness,
            alpha
        )
    }

    override fun visitHwbColor(ctx: CssPaintParser.HwbColorContext): CssPaint {
        val hueValue = ctx.hue().NUMBER().text
        val hueUnit = ctx.hue().DEG()?.text ?: ""
        val hue = CssAngle(hueValue, hueUnit)
        val whiteness = CssPercentFloat(ctx.whiteness().percentComponent().percent().NUMBER().text)
        val blackness = CssPercentFloat(ctx.blackness().percentComponent().percent().NUMBER().text)
        val alpha = CssFloat(ctx.alpha()?.text, 1f)
        return CssHwbColor(
            hue,
            whiteness,
            blackness,
            alpha
        )
    }

    override fun visitLabColor(ctx: CssPaintParser.LabColorContext): CssPaint {
        val lightness = CssFloat(ctx.lightness().text)
        val aAxis = CssFloat(ctx.labComponent(0)!!.text)
        val bAxis = CssFloat(ctx.labComponent(1)!!.text)
        val alpha = CssFloat(ctx.alpha()?.text, 1f)
        return if (ctx.labLike().OKLAB() != null) {
            CssOklabColor(lightness, aAxis, bAxis, alpha)
        } else {
            CssLabColor(lightness, aAxis, bAxis, alpha)
        }
    }

    override fun visitLchColor(ctx: CssPaintParser.LchColorContext): CssPaint {
        val lightness = CssFloat(ctx.lightness()?.text)
        val chroma = CssFloat(ctx.chroma()?.text)
        val hueValue = ctx.hue()?.NUMBER()?.text ?: "0"
        val hueUnit = ctx.hue()?.DEG()?.text ?: ""
        val hue = CssAngle(hueValue, hueUnit)
        val alpha = CssFloat(ctx.alpha()?.text, 1f)
        return if (ctx.lchLike().OKLCH() != null) {
            CssOklchColor(lightness, chroma, hue, alpha)
        } else {
            CssLchColor(lightness, chroma, hue, alpha)
        }
    }

    override fun visitNamedColor(ctx: CssPaintParser.NamedColorContext): CssPaint {
        val name = ctx.IDENTIFIER().text
        return CssColorKeywords[name]?.let { CssNamedColor(it, name) } ?: UnknownCssColor
    }

    override fun visitLightDark(ctx: CssPaintParser.LightDarkContext): CssPaint {
        return CssLightDark(visitBaseColor(ctx.baseColor(0)!!), visitBaseColor(ctx.baseColor(1)!!))
    }

    override fun visitRelativeComponent(ctx: CssPaintParser.RelativeComponentContext): CssRelativeComponent {
        return if (ctx.IDENTIFIER() != null) {
            CssRelativeComponent.Original(ctx.IDENTIFIER()!!.text)
        } else if (ctx.component() != null) {
            CssRelativeComponent.NewValue(ctx.component()!!.text)
        } else {
            CssRelativeComponent.Original("")
        }
    }

    fun parseColor(paintString: String): CssPaint? {
        lexer.inputStream = CharStreams.fromString(paintString)
        tokenStream.tokenSource = lexer
        parser.tokenStream = tokenStream

        parser.interpreter.predictionMode = PredictionMode.SLL
        parser.errorHandler = BailErrorStrategy()

        return try {
            val tree = parser.paint()
            visitPaint(tree)
        } catch (_: ParseCancellationException) {
            tokenStream.seek(0)
            parser.reset()
            parser.interpreter.predictionMode = PredictionMode.LL
            parser.errorHandler = DefaultErrorStrategy()
            try {
                val tree = parser.paint()
                visitPaint(tree)
            } catch (e: Exception) {
                CanvasEggLogger.errorThrowable(e)
                null
            }
        }
    }
}