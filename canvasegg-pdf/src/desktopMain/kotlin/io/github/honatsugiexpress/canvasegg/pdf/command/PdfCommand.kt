package io.github.honatsugiexpress.canvasegg.pdf.command

import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.RenderCommand
import io.github.honatsugiexpress.canvasegg.pdf.PDFBoxSvgToPdfConverter

sealed class PdfCommand(open val command: RenderCommand) {
    abstract fun draw(target: PDFBoxSvgToPdfConverter)
}