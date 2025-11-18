package com.honatsugiexp.canvasegg.svgz

import com.fleeksoft.ksoup.nodes.Document
import okio.Buffer
import okio.BufferedSink
import okio.GzipSink

actual class SvgzWriter actual constructor(val document: Document) {
    actual fun writeTo(sink: BufferedSink) {
        val gzipSink = GzipSink(sink)
        val buffer = Buffer()
        buffer.write(document.outerHtml().encodeToByteArray())
        gzipSink.write(buffer, buffer.size)
    }
}