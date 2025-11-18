package com.honatsugiexp.canvasegg.svgz

import com.fleeksoft.ksoup.nodes.Document
import okio.Buffer
import okio.BufferedSink

expect class SvgzWriter(document: Document) {
    fun writeTo(sink: BufferedSink)
}

fun SvgzWriter.byteArray(): ByteArray {
    val buffer = Buffer()
    writeTo(buffer)
    return buffer.readByteArray()
}