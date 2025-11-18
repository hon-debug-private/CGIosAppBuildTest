package com.honatsugiexp.canvasegg.pdf

import com.honatsugiexp.canvasegg.data.svg.parser.command.RenderCommand
import com.honatsugiexp.canvasegg.extendable.Page
import com.honatsugiexp.canvasegg.extendable.PageInfo
import com.honatsugiexp.canvasegg.extendable.SvgPageConverter
import okio.BufferedSink

abstract class SvgToPdfConverter: SvgPageConverter<PdfPageInfo, PdfPage>() {
    abstract override fun startPage(info: PdfPageInfo): PdfPage
    abstract override fun endPage()
    abstract fun writeTo(output: BufferedSink)
    abstract fun close()
}

abstract class PdfPageInfo(): PageInfo() {
    abstract val width: Int
    abstract val height: Int
}
abstract class PdfPage(): Page()