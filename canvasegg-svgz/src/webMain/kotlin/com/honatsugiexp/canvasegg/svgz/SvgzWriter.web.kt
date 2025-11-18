@file:OptIn(ExperimentalWasmJsInterop::class)

package com.honatsugiexp.canvasegg.svgz

import com.fleeksoft.ksoup.nodes.Document
import okio.BufferedSink
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsAny
import kotlin.js.JsModule
import kotlin.js.JsString
import kotlin.js.definedExternally

@JsModule("pako")
internal external object Pako {
    fun gzip(data: ByteArray, options: JsAny = definedExternally): ByteArray
    fun ungzip(data: ByteArray, options: JsAny = definedExternally): ByteArray
}

internal fun compressGzip(data: ByteArray): ByteArray {
    return Pako.gzip(data)
}

internal fun decompressGzip(data: ByteArray): ByteArray {
    return Pako.ungzip(data)
}

actual class SvgzWriter actual constructor(val document: Document) {
    actual fun writeTo(sink: BufferedSink) {
        val gzip = compressGzip(document.outerHtml().encodeToByteArray())
        sink.write(gzip)
    }
}