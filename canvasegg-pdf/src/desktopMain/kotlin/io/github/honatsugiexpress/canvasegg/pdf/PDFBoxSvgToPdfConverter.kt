package io.github.honatsugiexpress.canvasegg.pdf

import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.DrawableCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.HasTransformCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.RectCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.RenderCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.TextCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.parentList
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgStrokeData
import io.github.honatsugiexpress.canvasegg.pdf.command.PdfRectCommand
import io.github.honatsugiexpress.canvasegg.pdf.command.PdfTextCommand
import io.github.honatsugiexpress.canvasegg.pdf.util.convert.paint.toPDLineCap
import io.github.honatsugiexpress.canvasegg.pdf.util.convert.paint.toPDLineJoin
import io.github.honatsugiexpress.canvasegg.pdf.util.convert.transform.applyContentStream
import io.github.honatsugiexpress.canvasegg.pdf.util.data.paint.EmptyPDColor
import io.github.honatsugiexpress.canvasegg.pdf.util.data.paint.SvgPDColorEnv
import io.github.honatsugiexpress.canvasegg.pdf.util.data.paint.toPDColor
import okio.BufferedSink
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.PDResources
import org.apache.pdfbox.pdmodel.common.PDRectangle


class PDFBoxSvgToPdfConverter(val document: PDDocument): SvgToPdfConverter() {
    var currentPage: PDPage? = null
    var currentStream: PDPageContentStream? = null
    override fun startPage(info: PdfPageInfo): PdfPage {
        val page = PDPage(
            PDRectangle(
                info.width.toFloat(),
                info.height.toFloat()
            )
        )
        page.resources = PDResources()
        document.addPage(page)
        currentPage = page
        currentStream = PDPageContentStream(document, page)
        return PDFBoxPage(page)
    }

    override fun endPage() {
        currentStream?.close()
        currentPage = null
        currentStream = null
    }

    override fun close() {
        document.close()
    }

    override fun writeTo(output: BufferedSink) {
        document.save(output.outputStream())
    }

    override fun processRect(command: RectCommand) {
        super.processRect(command)
        PdfRectCommand(command).draw(this)
    }

    override fun processText(command: TextCommand) {
        super.processText(command)
        PdfTextCommand(command).draw(this)
    }

    internal fun applyParentCommand(command: RenderCommand, block: () -> Unit) {
        val stream = currentStream ?: return
        var transformCount = 0
        command.parentList()
            .asSequence()
            .filterIsInstance<HasTransformCommand>()
            .flatMap {
                it.transformCommands()
            }
            .forEach {
                it.applyContentStream(stream)
                transformCount++
            }
        block()
        repeat(transformCount + 1) {
            stream.restoreGraphicsState()
        }
    }

    internal fun applyStrokeData(command: DrawableCommand, strokeData: SvgStrokeData?) = currentStream?.let { stream ->
        if (strokeData == null) return@let
        val pdColorEnv = SvgPDColorEnv(currentPage!!.resources)
        stream.setStrokingColor(strokeData.paint.toPDColor(pdColorEnv))
        stream.setLineWidth(strokeData.stroke.width)
        stream.setLineCapStyle(strokeData.stroke.cap.toPDLineCap())
        stream.setLineJoinStyle(strokeData.stroke.join.toPDLineJoin())
        stream.setMiterLimit(strokeData.stroke.miter)
        SvgStrokeData.dashPathEffect(command, 1.0f)?.let {
            stream.setLineDashPattern(it.first, it.second)
        } ?: stream.setLineDashPattern(floatArrayOf(), 0f)
    }

    internal fun resetStroke() = currentStream?.let { stream ->
        stream.setStrokingColor(EmptyPDColor)
        stream.setLineWidth(0f)
        stream.setLineCapStyle(0)
        stream.setLineJoinStyle(0)
        stream.setMiterLimit(4f)
        stream.setLineDashPattern(floatArrayOf(), 0f)
    }
}