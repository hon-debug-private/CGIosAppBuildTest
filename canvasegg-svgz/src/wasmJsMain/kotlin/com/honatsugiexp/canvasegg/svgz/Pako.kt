@file:OptIn(ExperimentalWasmJsInterop::class)

package com.honatsugiexp.canvasegg.svgz

import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsAny
import kotlin.js.JsModule
import kotlin.js.definedExternally

@OptIn(ExperimentalWasmJsInterop::class)
@JsModule("pako")
internal external object Pako: JsAny {
    fun gzip(data: ByteArray, options: JsAny = definedExternally): ByteArray
    fun ungzip(data: ByteArray, options: JsAny = definedExternally): ByteArray
}

internal fun compressGzip(data: ByteArray): ByteArray {
    return Pako.gzip(data)
}

internal fun decompressGzip(data: ByteArray): ByteArray {
    return Pako.ungzip(data)
}