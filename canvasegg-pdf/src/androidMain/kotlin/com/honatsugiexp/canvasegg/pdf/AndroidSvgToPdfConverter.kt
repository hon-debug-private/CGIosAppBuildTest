package com.honatsugiexp.canvasegg.pdf

import android.graphics.pdf.PdfDocument
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import com.honatsugiexp.canvasegg.data.svg.parser.command.DrawableCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.HasParentCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.HasTransformCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.RenderCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.parentListWithSelf
import com.honatsugiexp.canvasegg.data.svg.parser.command.transform.applyCommandToDrawTransform
import okio.BufferedSink

class AndroidSvgToPdfConverter(val nativeDocument: PdfDocument): SvgToPdfConverter() {
    var currentPage: AndroidPdfPage? = null
    var currentDrawScope: DrawScope? = null
    override fun startPage(info: PdfPageInfo): PdfPage {
        return AndroidPdfPage(
            nativeDocument.startPage(
                (info as AndroidPdfPageInfo).nativePageInfo
            )
        ).also {
            currentPage = it
            currentDrawScope = CanvasDrawScope().apply {
                drawContext.canvas = Canvas(it.nativePage.canvas)
            }
        }
    }

    override fun endPage() {
        nativeDocument.finishPage(currentPage!!.nativePage)
        currentPage = null
        currentDrawScope = null
    }

    override fun processCommand(command: RenderCommand) {
        super.processCommand(command)
        if (command is DrawableCommand) {
            if (command is HasParentCommand) {
                currentDrawScope?.withTransform(
                    transformBlock = {
                        command
                            .parentListWithSelf()
                            .filterIsInstance<HasTransformCommand>()
                            .forEach { transformCommand ->
                                transformCommand.transformCommands().forEach {
                                    applyCommandToDrawTransform(this, it)
                                }
                            }
                    }
                ) {
                    command.draw(currentDrawScope!!)
                }
            } else {
                command.draw(currentDrawScope!!)
            }
        }
    }

    override fun writeTo(output: BufferedSink) {
        nativeDocument.writeTo(output.outputStream())
    }

    override fun close() {
        nativeDocument.close()
    }
}

class AndroidPdfPageInfo(internal val nativePageInfo: PdfDocument.PageInfo): PdfPageInfo() {
    override val width: Int
        get() = nativePageInfo.pageWidth
    override val height: Int
        get() = nativePageInfo.pageHeight
}

class AndroidPdfPage(internal val nativePage: PdfDocument.Page): PdfPage()