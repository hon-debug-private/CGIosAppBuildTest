package io.github.honatsugiexpress.canvasegg.pdf

import org.apache.pdfbox.pdmodel.PDDocument

actual fun SvgToPdfConverter.Companion.of(): SvgToPdfConverter =
    PDFBoxSvgToPdfConverter(PDDocument())

actual fun PdfPageInfo.Companion.of(
    width: Int,
    height: Int,
    pageNumber: Int
): PdfPageInfo = PDFBoxPageInfo(width, height)