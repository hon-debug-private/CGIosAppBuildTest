package com.honatsugiexp.canvasegg.pdf

import com.honatsugiexp.canvasegg.data.svg.parser.command.RectCommand
import com.honatsugiexp.canvasegg.data.svg.type.SvgPaint
import okio.BufferedSink
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream



class PDFBoxSvgToPdfConverter(val document: PDDocument): SvgToPdfConverter() {
    var currentPage: PDPage? = null
    var currentStream: PDPageContentStream? = null
    override fun startPage(info: PdfPageInfo): PdfPage {
        val page = PDPage()
        document.addPage(page)
        currentPage = page
        currentStream = PDPageContentStream(document, page)
        return PDFBoxPage(page)
    }

    override fun endPage() {
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
        val fill = SvgPaint.resolve(command.element, "fill")
        currentStream
    }
}