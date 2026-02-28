package io.github.honatsugiexpress.canvasegg.pdf.command

import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.RectCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.RenderCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.xLenEnv
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.yLenEnv
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgLength
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgPaint
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgStrokeData
import io.github.honatsugiexpress.canvasegg.data.svg.type.toPxValue
import io.github.honatsugiexpress.canvasegg.pdf.PDFBoxSvgToPdfConverter
import io.github.honatsugiexpress.canvasegg.pdf.util.data.paint.SvgPDColorEnv
import io.github.honatsugiexpress.canvasegg.pdf.util.data.paint.toPDColor

class PdfRectCommand(override val command: RectCommand): PdfCommand(command) {
    override fun draw(target: PDFBoxSvgToPdfConverter) {
        with(target) {
            currentStream?.let { stream ->
                val pdColorEnv = SvgPDColorEnv(currentPage!!.resources)
                val styleController = command.styleData

                val fill = SvgPaint.resolve(command, "fill").toPDColor(pdColorEnv)
                val strokeData = SvgStrokeData.resolve(command, 1.0f)
                stream.setNonStrokingColor(fill)
                applyStrokeData(command, strokeData) ?: resetStroke()
                val x = SvgLength(styleController.attrOrStyleOrNull("x")).toPxValue(command.xLenEnv)
                val y = SvgLength(styleController.attrOrStyleOrNull("y")).toPxValue(command.yLenEnv)
                val width =
                    SvgLength(styleController.attrOrStyleOrNull("width")).toPxValue(command.xLenEnv)
                val height =
                    SvgLength(styleController.attrOrStyleOrNull("height")).toPxValue(command.yLenEnv)
                val rx =
                    SvgLength(styleController.attrOrStyleOrNull("rx")).toPxValue(command.xLenEnv)
                val ry =
                    SvgLength(styleController.attrOrStyleOrNull("rx")).toPxValue(command.yLenEnv)
                if (rx == 0f && ry == 0f) {
                    applyParentCommand(command) {
                        stream.addRect(x, y, width, height)
                    }
                } else {
                    applyParentCommand(command) {
                        stream.moveTo(x + rx, y)
                        stream.lineTo(x + width - rx, y)
                        stream.curveTo(x + width, y, x + width, y + ry, x + width, y + ry)
                        stream.lineTo(x + width, y + height - ry)
                        stream.curveTo(
                            x + width,
                            y + height,
                            x + width - rx,
                            y + height,
                            x + width - rx,
                            y + height
                        )
                        stream.lineTo(x + rx, y + height)
                        stream.curveTo(x, y + height, x, y + height - ry, x, y + height - ry)
                        stream.lineTo(x, y + ry)
                        stream.curveTo(x, y, x + rx, y, x + rx, y)
                        stream.closePath()
                    }
                }
                strokeData?.let {
                    stream.fillAndStroke()
                } ?: stream.fill()
            }
        }
    }
}