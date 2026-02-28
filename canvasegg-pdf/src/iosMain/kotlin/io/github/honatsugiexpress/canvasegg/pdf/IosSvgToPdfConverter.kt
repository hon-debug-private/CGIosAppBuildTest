package io.github.honatsugiexpress.canvasegg.pdf

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import okio.BufferedSink
import platform.CoreGraphics.CGRectMake
import platform.PDFKit.PDFDocument
import platform.PDFKit.PDFPage
import platform.posix.memcpy

class IosSvgToPdfConverter(val nativeDocument: PDFDocument): SvgToPdfConverter() {
    var currentPage: PDFPage? = null
    @OptIn(ExperimentalForeignApi::class)
    override fun startPage(info: PdfPageInfo): PdfPage {
        return PDFPage().let { page ->
            page.setBounds(CGRectMake(
                x = 0.0,
                y = 0.0,
                width = info.width.toDouble(),
                height = info.height.toDouble()
            ))
            currentPage = page
            nativeDocument.insertPage(page, nativeDocument.pageCount + 1u)
            IosPdfPage(page)
        }
    }

    override fun endPage() {
        currentPage = null
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun writeTo(output: BufferedSink) {
        val data = nativeDocument.dataRepresentation() ?: return
        val bytes = ByteArray(data.length.toInt()).apply {
            usePinned {
                memcpy(it.addressOf(0), data.bytes, data.length)
            }
        }
        output.write(bytes)
    }

    override fun close() {}
}

class IosPdfPageInfo(
    override val width: Int,
    override val height: Int
): PdfPageInfo()

class IosPdfPage(internal val nativePage: PDFPage): PdfPage()