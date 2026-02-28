@file:OptIn(ExperimentalWasmJsInterop::class)

package io.github.honatsugiexpress.canvasegg.svgz

import org.khronos.webgl.Int8Array
import org.khronos.webgl.toByteArray
import org.khronos.webgl.toInt8Array
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsAny
import kotlin.js.JsModule
import kotlin.js.definedExternally

@OptIn(ExperimentalWasmJsInterop::class)
@JsModule("pako")
internal external object Pako: JsAny {
    fun gzip(data: Int8Array, options: JsAny = definedExternally): Int8Array
    fun ungzip(data: Int8Array, options: JsAny = definedExternally): Int8Array
}

internal fun compressGzip(data: ByteArray): ByteArray {
    return Pako.gzip(data.toInt8Array()).toByteArray()
}

internal fun decompressGzip(data: ByteArray): ByteArray {
    return Pako.ungzip(data.toInt8Array()).toByteArray()
}