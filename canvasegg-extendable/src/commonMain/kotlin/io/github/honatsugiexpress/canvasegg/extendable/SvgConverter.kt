package io.github.honatsugiexpress.canvasegg.extendable

import com.fleeksoft.ksoup.nodes.Document
import io.github.honatsugiexpress.canvasegg.data.svg.parser.SvgCanvasParser
import io.github.honatsugiexpress.canvasegg.data.svg.parser.SvgParserEnv
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.GroupCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.RectCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.RenderCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.RenderEnv
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.TextCommand

abstract class SvgConverter() {
    fun parse(document: Document, env: SvgParserEnv) {
        val parser = SvgCanvasParser(document, env)
        val commands = RenderCommand.parseDocument(
            document = document,
            env = RenderEnv(parser, env.density.density, env.textMeasurer)
        )
        commands.forEach {
            when (it) {
                is GroupCommand -> processGroup(it)
                is RectCommand -> processRect(it)
                is TextCommand -> processText(it)
                else -> {}
            }
        }
    }
    open fun processCommand(command: RenderCommand) {}
    open fun processGroup(command: GroupCommand) { processCommand(command) }
    open fun processRect(command: RectCommand) { processCommand(command) }
    open fun processText(command: TextCommand) { processCommand(command) }
}