package com.honatsugiexp.canvasegg.pdf

import okio.BufferedSink
import platform.CoreGraphics.CGContextRef
import platform.Foundation.NSMutableData
import platform.PDFKit.PDFDocument
import platform.PDFKit.PDFPage

class IosSvgToPdfConverter(val nativeDocument: PDFDocument): SvgToPdfConverter() {
    var currentPage: PDFPage? = null
    override fun startPage(info: IosPdfPageInfo): IosPdfPage? {
        return IosPdfPage(
            nativeDocument.pageAtIndex(nativeDocument.pageCount + 1u)
        ).also {
            currentPage = it.nativePage
        }
    }
}

class IosPdfPageInfo(
    override val width: Int,
    override val height: Int
): PdfPageInfo()

class IosPdfPage(internal val nativePage: PDFPage): PdfPage()