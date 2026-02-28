package io.github.honatsugiexpress.canvasegg.data.svg.parser.command

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.fleeksoft.ksoup.nodes.Element
import io.github.honatsugiexpress.canvasegg.data.css.ElementStyleData
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.fontFamily
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.fontSize
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.fontWeight
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.lenEnv
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.xLenEnv
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.yLenEnv
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.transform.applyCommand
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgLength
import io.github.honatsugiexpress.canvasegg.data.svg.type.toPxValue
import io.github.honatsugiexpress.canvasegg.data.svg.type.toScreenValue

class TextCommand(
    override val env: RenderEnv,
    val content: MutableList<TextContentCommand>,
    override val parent: RenderCommand?,
    override val element: Element
): HasSealedChildCommand<TextContentCommand>(env, content, parent),
    DrawableCommand,
    ElementCommand {
    override val styleData: ElementStyleData = styleData()
    override fun draw(drawScope: DrawScope) {
        if (content.isNotEmpty()) {
            val textNodes = element
                .textNodes()
                .map {
                    TextNodeContent(
                        env,
                        this,
                        it
                    )
                }
            val result = (content + textNodes).sortedBy {
                it.textIndex
            }

        } else {
            val x = SvgLength(styleData.attrOrStyleOrNull("x")).toScreenValue(xLenEnv, env.density)
            val y = SvgLength(styleData.attrOrStyleOrNull("y")).toScreenValue(yLenEnv, env.density)
            val fill = io.github.honatsugiexpress.canvasegg.data.svg.type.SvgPaint.resolve(this, "fill")
            val strokeData = io.github.honatsugiexpress.canvasegg.data.svg.type.SvgStrokeData.resolve(this, env.density)
            val fontFamily = env
                .fontFaces
                .filter {
                    println(it.fontFamily in fontFamily)
                    println("${it.fontFamily} $fontFamily")
                    it.fontFamily in fontFamily
                }
                .firstNotNullOfOrNull { declaration ->
                    val fontFace = io.github.honatsugiexpress.canvasegg.data.svg.type.font.SvgFontFace.fromDeclaration(declaration)
                    env.uriResolver.firstNotNullOfOrNull { resolver ->
                        println(resolver.resolveFont(fontFace))
                        resolver.resolveFont(fontFace)
                    }
                } ?: FontFamily.Default
            println(fontFamily)
            drawScope.drawText(
                textMeasurer = env.textMeasurer,
                text = element.text(),
                topLeft = Offset(x, y),
                style = when (fill) {
                    is io.github.honatsugiexpress.canvasegg.data.svg.type.SvgPaint.Color -> TextStyle(
                        color = fill.color,
                        fontSize = (fontSize?.toPxValue(lenEnv)?.sp
                            ?: TextUnit.Unspecified) / env.density,
                        fontWeight = fontWeight,
                        fontFamily = fontFamily
                    )

                    is io.github.honatsugiexpress.canvasegg.data.svg.type.SvgPaint.BrushPaint -> TextStyle(
                        brush = fill.toBrush(),
                        fontSize = (fontSize?.toPxValue(lenEnv)?.sp
                            ?: TextUnit.Unspecified) / env.density,
                        fontWeight = fontWeight,
                        fontFamily = fontFamily
                    )

                    else -> TextStyle.Default
                }
            )
            strokeData?.let {
                drawScope.drawText(
                    textMeasurer = env.textMeasurer,
                    text = element.text(),
                    topLeft = Offset(x, y),
                    style = when (val strokePaint = strokeData.paint) {
                        is io.github.honatsugiexpress.canvasegg.data.svg.type.SvgPaint.Color -> TextStyle(
                            color = strokePaint.color,
                            fontSize = (fontSize?.toPxValue(lenEnv)?.sp
                                ?: TextUnit.Unspecified) / env.density,
                            fontWeight = fontWeight,
                            fontFamily = fontFamily
                        )

                        is io.github.honatsugiexpress.canvasegg.data.svg.type.SvgPaint.BrushPaint -> TextStyle(
                            brush = strokePaint.toBrush(),
                            fontSize = (fontSize?.toPxValue(lenEnv)?.sp
                                ?: TextUnit.Unspecified) / env.density,
                            fontWeight = fontWeight,
                            fontFamily = fontFamily
                        )

                        else -> TextStyle.Default
                    }
                )
            }
        }
    }

    override fun path(): Path {
        return Path().apply {
            parentList()
                .asSequence()
                .filterIsInstance<HasTransformCommand>()
                .flatMap {
                    it.transformCommands()
                }
                .forEach {
                    applyCommand(it)
                }
            addPath(nativePath())
        }
    }
}

internal expect fun TextCommand.nativePath(): Path