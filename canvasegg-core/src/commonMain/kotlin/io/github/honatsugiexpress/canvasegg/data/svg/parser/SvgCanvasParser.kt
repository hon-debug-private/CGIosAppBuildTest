package io.github.honatsugiexpress.canvasegg.data.svg.parser

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.nodes.Element
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.DrawableCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ElementCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.HasTransformCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.PathCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.RenderCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.RenderEnv
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.parentListWithSelf
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.transform.applyCommand

class SvgCanvasParser(
    val document: Document,
    val env: SvgParserEnv
) {
    fun draw(drawScope: DrawScope) {
        val commands = RenderCommand.parseDocument(
            document = document,
            env = RenderEnv(
                this,
                env.density.density,
                env.textMeasurer
            )
        )
        drawScope.withTransform(
            transformBlock = {
                if (env.option.scaleX != 1f || env.option.scaleY != 1f) {
                    scale(env.option.scaleX, env.option.scaleY)
                }
                env.option.drawArea?.let {
                    clipPath(it)
                }
            }
        ) {
            commands.forEach { command ->
                if (command is DrawableCommand) {
                    if (command.parent != null) {
                        drawScope.withTransform(
                            transformBlock = {
                                command
                                    .parentListWithSelf()
                                    .filterIsInstance<HasTransformCommand>()
                                    .forEach { transformCommand ->
                                        transformCommand.transformCommands().forEach {
                                            applyCommand(it)
                                        }
                                    }
                            }
                        ) {
                            command.draw(drawScope)
                        }
                    } else {
                        command.draw(drawScope)
                    }
                }
            }
        }
    }

    fun path(element: Element): Path? {
        val commands = RenderCommand.parseDocument(
            document = document,
            env = RenderEnv(
                this,
                env.density.density,
                env.textMeasurer
            )
        )
        val target = commands.firstOrNull {
            it is ElementCommand && it.element == element
        }
        return when (target) {
            is PathCommand -> target.path()
            else -> null
        }
    }

    override fun toString(): String {
        return "SvgCanvasParser(document=$document, env=$env)"
    }
}

fun SvgCanvasParser.hitTest(offset: Offset, zindex: Int): Element? {
    val stack = ArrayDeque<Element>()
    stack.add(document)

    val elements = mutableListOf<Element>()

    while (stack.isNotEmpty()) {
        val currentElement = stack.removeLast()
        elements.add(currentElement)
        val children = currentElement.children()
        for (child in children) {
            stack.add(child)
        }
    }
    var result: Element? = null
    var zIndexCount = 0
    val x = offset.x
    val y = offset.y
    for (element in elements) {
        val successHitTest = path(element)?.hitTest(x, y) == true
        if (successHitTest) {
            if (zIndexCount >= zindex) {
                result = element
                break
            } else {
                zIndexCount++
            }
        }
    }
    return result
}

expect fun Path.hitTest(x: Float, y: Float): Boolean