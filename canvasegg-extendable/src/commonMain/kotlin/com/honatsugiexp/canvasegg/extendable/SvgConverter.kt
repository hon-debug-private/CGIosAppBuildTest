package com.honatsugiexp.canvasegg.extendable

import com.fleeksoft.ksoup.nodes.Document
import com.honatsugiexp.canvasegg.data.svg.parser.SvgCanvasParser
import com.honatsugiexp.canvasegg.data.svg.parser.SvgParserEnv
import com.honatsugiexp.canvasegg.data.svg.parser.command.GroupCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.RectCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.RenderCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.RenderEnv

abstract class SvgConverter() {
    fun parse(document: Document, env: SvgParserEnv) {
        val parser = SvgCanvasParser(document, env)
        val commands = RenderCommand.parseDocument(
            document = document,
            env = RenderEnv(parser, env.density.density)
        )
        commands.forEach {
            when (it) {
                is GroupCommand -> processGroup(it)
                is RectCommand -> processRect(it)
                else -> {}
            }
        }
    }
    open fun processCommand(command: RenderCommand) {}
    open fun processGroup(command: GroupCommand) {
        processCommand(command)
    }
    open fun processRect(command: RectCommand) {
        processCommand(command)
    }
}