package io.github.honatsugiexpress.canvasegg.common.log

import android.util.Log

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

    actual companion object Default :
        CanvasEggLogger {
        const val TAG_NAME = "CanvasEgg"
        actual override fun info(message: String) {
            Log.i(TAG_NAME, message)
        }

        actual override fun infoObj(obj: Any?) {
            Log.i(TAG_NAME, obj.toString())
        }

        actual override fun warn(message: String) {
            Log.w(TAG_NAME, message)
        }

        actual override fun warnObj(obj: Any?) {
            Log.w(TAG_NAME, obj.toString())
        }

        actual override fun warnThrowable(throwable: Throwable) {
            Log.w(TAG_NAME, throwable)
        }

        actual override fun error(message: String) {
            Log.e(TAG_NAME, message)
        }

        actual override fun errorObj(obj: Any?) {
            Log.e(TAG_NAME, obj.toString())
        }

        actual override fun errorThrowable(throwable: Throwable) {
            Log.e(TAG_NAME, null, throwable)
        }

        actual override fun debug(message: String) {
            Log.d(TAG_NAME, message)
        }

        actual override fun debugObj(obj: Any?) {
            Log.e(TAG_NAME, obj.toString())
        }

        actual override fun tag(tag: String): CanvasEggLogger {
            return newTagLogger(tag)
        }
        private fun newTagLogger(tagName: String): CanvasEggLogger {
            return object : CanvasEggLogger {
                override fun info(message: String) {
                    Log.i(tagName, message)
                }

                override fun infoObj(obj: Any?) {
                    Log.i(tagName, obj.toString())
                }

                override fun warn(message: String) {
                    Log.w(tagName, message)
                }

                override fun warnObj(obj: Any?) {
                    Log.w(tagName, obj.toString())
                }

                override fun warnThrowable(throwable: Throwable) {
                    Log.w(tagName, throwable)
                }

                override fun error(message: String) {
                    Log.e(tagName, message)
                }

                override fun errorObj(obj: Any?) {
                    Log.e(tagName, obj.toString())
                }

                override fun errorThrowable(throwable: Throwable) {
                    Log.e(tagName, null, throwable)
                }

                override fun debug(message: String) {
                    Log.d(tagName, message)
                }

                override fun debugObj(obj: Any?) {
                    Log.e(tagName, obj.toString())
                }

                override fun tag(tag: String): CanvasEggLogger {
                    return newTagLogger(tag)
                }
            }
        }
    }
}