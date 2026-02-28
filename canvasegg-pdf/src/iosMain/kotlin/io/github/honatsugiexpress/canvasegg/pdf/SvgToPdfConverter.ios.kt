package io.github.honatsugiexpress.canvasegg.pdf

import platform.PDFKit.PDFDocument

actual fun SvgToPdfConverter.Companion.of(): SvgToPdfConverter =
    IosSvgToPdfConverter(PDFDocument())

actual fun PdfPageInfo.Companion.of(
    width: Int,
    height: Int,
    pageNumber: Int
): PdfPageInfo = IosPdfPageInfo(width, height)