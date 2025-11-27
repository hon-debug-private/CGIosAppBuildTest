package com.honatsugiexp.canvasegg.data.svg.parser

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.withTransform
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.nodes.Element
import com.honatsugiexp.canvasegg.data.svg.parser.command.HasChildPathCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.DrawableCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.ElementCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.HasParentCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.HasTransformCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.PathCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.RenderCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.RenderEnv
import com.honatsugiexp.canvasegg.data.svg.parser.command.parentListWithSelf
import com.honatsugiexp.canvasegg.data.svg.parser.command.transform.applyCommand
import com.honatsugiexp.canvasegg.data.svg.resolver.SvgUriResolver

class SvgCanvasParser(
    val document: Document,
    val env: SvgParserEnv
) {
    internal var density: Float = 1f
    val uriResolvers = mutableListOf<SvgUriResolver>()

    fun draw(drawScope: DrawScope) {
        val commands = RenderCommand.parseDocument(
            document = document,
            env = RenderEnv(this, density)
        )
        val drawBlock = {
            commands.forEach { command ->
                if (command is DrawableCommand) {
                    if (command is HasParentCommand) {
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
        env.option.drawArea?.let {
            drawScope.clipPath(it) {
                drawBlock()
            }
        } ?: drawBlock()
    }

    fun path(element: Element): Path? {
        val commands = RenderCommand.parseDocument(
            document = document,
            env = RenderEnv(this, density)
        )
        val target = commands.firstOrNull {
            it is ElementCommand && it.element == element
        }
        return when (target) {
            is PathCommand -> target.path()
            is HasChildPathCommand -> target.childrenPath()
            else -> null
        }
    }

    override fun toString(): String {
        return "SvgCanvasParser(document=$document, density=$density, uriResolver=$uriResolvers)"
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