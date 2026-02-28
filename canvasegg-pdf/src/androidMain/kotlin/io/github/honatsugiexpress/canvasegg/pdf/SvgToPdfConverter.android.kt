package io.github.honatsugiexpress.canvasegg.pdf

import android.graphics.pdf.PdfDocument

actual fun SvgToPdfConverter.Companion.of(): SvgToPdfConverter =
    AndroidSvgToPdfConverter(PdfDocument())

actual fun PdfPageInfo.Companion.of(
    width: Int,
    height: Int,
    pageNumber: Int
): PdfPageInfo = AndroidPdfPageInfo(PdfDocument.PageInfo.Builder(width, height, pageNumber).create())