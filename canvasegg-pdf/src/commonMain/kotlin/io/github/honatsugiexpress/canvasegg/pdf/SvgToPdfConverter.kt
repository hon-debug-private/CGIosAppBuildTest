package io.github.honatsugiexpress.canvasegg.pdf

import io.github.honatsugiexpress.canvasegg.extendable.Page
import io.github.honatsugiexpress.canvasegg.extendable.PageInfo
import io.github.honatsugiexpress.canvasegg.extendable.SvgPageConverter
import okio.BufferedSink

abstract class SvgToPdfConverter: SvgPageConverter<PdfPageInfo, PdfPage>() {
    abstract override fun startPage(info: PdfPageInfo): PdfPage
    abstract override fun endPage()
    abstract fun writeTo(output: BufferedSink)
    abstract fun close()
    companion object
}

expect fun SvgToPdfConverter.Companion.of(): SvgToPdfConverter

abstract class PdfPageInfo(): PageInfo() {
    abstract val width: Int
    abstract val height: Int
    companion object
}

expect fun PdfPageInfo.Companion.of(width: Int, height: Int, pageNumber: Int): PdfPageInfo

abstract class PdfPage(): Page()