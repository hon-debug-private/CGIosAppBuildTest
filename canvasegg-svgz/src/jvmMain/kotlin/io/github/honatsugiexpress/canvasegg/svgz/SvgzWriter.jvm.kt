package io.github.honatsugiexpress.canvasegg.svgz

import com.fleeksoft.ksoup.nodes.Document
import okio.Buffer
import okio.BufferedSink
import okio.GzipSink
import okio.buffer
import okio.use

actual class SvgzWriter actual constructor(val document: Document) {
    actual fun writeTo(sink: BufferedSink) {
        GzipSink(sink).buffer().use {
            it.writeUtf8(document.outerHtml())
        }
    }
}