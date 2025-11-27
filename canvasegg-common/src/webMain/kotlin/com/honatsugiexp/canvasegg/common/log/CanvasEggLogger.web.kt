@file:OptIn(ExperimentalWasmJsInterop::class)

package com.honatsugiexp.canvasegg.common.log

import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsAny
import kotlin.js.toJsString

@OptIn(ExperimentalWasmJsInterop::class)
external class Console: JsAny {
    fun info(vararg obj: JsAny?)
    fun warn(vararg obj: JsAny?)
    fun error(vararg obj: JsAny?)
    fun debug(vararg obj: JsAny?)
}

external val console: Console

actual interface CanvasEggLogger {
    actual fun info(message: String)
    actual fun infoObj(obj: Any?)
    actual fun warn(message: String)
    actual fun warnObj(obj: Any?)
    actual fun warnThrowable(throwable: Throwable)
    actual fun error(message: String)
    actual fun errorObj(obj: Any?)
    actual fun errorThrowable(throwable: Throwable)
    actual fun debug(message: String)
    actual fun debugObj(obj: Any?)
    actual fun tag(tag: String): CanvasEggLogger

    actual companion object Default : CanvasEggLogger {
        val tagName = "CanvasEgg".toJsString()
        actual override fun info(message: String) {
            console.info(tagName, message.toJsString())
        }

        actual override fun infoObj(obj: Any?) {
            console.info(tagName, obj?.toString()?.toJsString())
        }

        actual override fun warn(message: String) {
            console.warn(tagName, message.toJsString())
        }

        actual override fun warnObj(obj: Any?) {
            console.warn(tagName, obj?.toString()?.toJsString())
        }

        actual override fun warnThrowable(throwable: Throwable) {
            console.warn(tagName, throwable.stackTraceToString().toJsString())
        }

        actual override fun error(message: String) {
            console.error(tagName, message.toJsString())
        }

        actual override fun errorObj(obj: Any?) {
            console.error(tagName, obj?.toString()?.toJsString())
        }

        actual override fun errorThrowable(throwable: Throwable) {
            console.error(tagName, throwable.stackTraceToString().toJsString())
        }

        actual override fun debug(message: String) {
            console.debug(tagName, message.toJsString())
        }

        actual override fun debugObj(obj: Any?) {
            console.debug(tagName, obj?.toString()?.toJsString())
        }

        actual override fun tag(tag: String): CanvasEggLogger {
            return createNewLogger(tag)
        }
        private fun createNewLogger(tag: String): CanvasEggLogger {
            val tagName = "$tag:".toJsString()
            return object : CanvasEggLogger {
                override fun info(message: String) {
                    console.info(tagName, message.toJsString())
                }

                override fun infoObj(obj: Any?) {
                    console.info(tagName, obj?.toString()?.toJsString())
                }

                override fun warn(message: String) {
                    console.warn(tagName, message.toJsString())
                }

                override fun warnObj(obj: Any?) {
                    console.warn(tagName, obj?.toString()?.toJsString())
                }

                override fun warnThrowable(throwable: Throwable) {
                    console.warn(tagName, throwable.stackTraceToString().toJsString())
                }

                override fun error(message: String) {
                    console.error(tagName, message.toJsString())
                }

                override fun errorObj(obj: Any?) {
                    console.error(tagName, obj?.toString()?.toJsString())
                }

                override fun errorThrowable(throwable: Throwable) {
                    console.error(tagName, throwable.toString().toJsString())
                }

                override fun debug(message: String) {
                    console.debug(tagName, message.toJsString())
                }

                override fun debugObj(obj: Any?) {
                    console.debug(tagName, obj?.toString()?.toJsString())
                }

                override fun tag(tag: String): CanvasEggLogger {
                    return createNewLogger(tag)
                }
            }
        }

    }
}