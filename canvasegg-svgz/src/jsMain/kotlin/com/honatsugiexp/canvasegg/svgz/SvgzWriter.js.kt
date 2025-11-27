package com.honatsugiexp.canvasegg.svgz

import com.fleeksoft.ksoup.nodes.Document
import okio.BufferedSink

actual class SvgzWriter actual constructor(val document: Document) {
    actual fun writeTo(sink: BufferedSink) {
        val gzip = compressGzip(document.outerHtml().encodeToByteArray())
        sink.write(gzip)
    }
}