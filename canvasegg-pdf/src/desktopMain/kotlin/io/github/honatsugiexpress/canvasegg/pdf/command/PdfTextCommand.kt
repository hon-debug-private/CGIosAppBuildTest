package io.github.honatsugiexpress.canvasegg.pdf.command

import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.TextCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.fontSize
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.lenEnv
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgPaint
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgStrokeData
import io.github.honatsugiexpress.canvasegg.data.svg.type.toScreenValue
import io.github.honatsugiexpress.canvasegg.pdf.PDFBoxSvgToPdfConverter
import io.github.honatsugiexpress.canvasegg.pdf.util.data.paint.SvgPDColorEnv
import io.github.honatsugiexpress.canvasegg.pdf.util.data.paint.toPDColor
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.font.Standard14Fonts

class PdfTextCommand(override val command: TextCommand): PdfCommand(command) {
    override fun draw(target: PDFBoxSvgToPdfConverter) {
        with (target) {
            currentStream?.let { stream ->
                val pdColorEnv = SvgPDColorEnv(currentPage!!.resources)
                val styleController = command.styleData
                val fill = SvgPaint.resolve(command, "fill").toPDColor(pdColorEnv)
                val strokeData = SvgStrokeData.resolve(command, 1.0f)
                stream.setNonStrokingColor(fill)
                applyStrokeData(command, strokeData) ?: resetStroke()
                val text = command.element.text()
                applyParentCommand(command) {
                    stream.beginText()
                    val font = PDType1Font(Standard14Fonts.FontName.HELVETICA)
                    stream.setFont(
                        font,
                        command.fontSize?.toScreenValue(
                            command.lenEnv,
                            command.env.density
                        ) ?: 16f
                    )
                    stream.showText(text)
                    stream.endText()
                }
                strokeData?.let {
                    stream.fillAndStroke()
                } ?: stream.fill()
            }
        }
    }
}