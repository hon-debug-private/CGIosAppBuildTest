@file:OptIn(ExperimentalWasmJsInterop::class)

package io.github.honatsugiexpress.canvasegg.pdf.interop

external class JsPdf: JsAny {
    fun rect(x: JsNumber, y: JsNumber, w: JsNumber, h: JsNumber): JsPdf
    fun fillStroke()
}

external fun jsPDF(): JsPdf