@file:OptIn(ExperimentalNativeApi::class)

package io.github.honatsugiexpress.canvasegg.common.log

import platform.Foundation.NSLog
import kotlin.experimental.ExperimentalNativeApi

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
        const val TAG_NAME = "VecEdit"

        actual override fun info(message: String) {
            NSLog("I/%s: %s", TAG_NAME, message)
        }

        actual override fun infoObj(obj: Any?) {
            // オブジェクトを文字列化
            NSLog("I/%s: %s", TAG_NAME, obj?.toString() ?: "null")
        }

        actual override fun warn(message: String) {
            NSLog("W/%s: %s", TAG_NAME, message)
        }

        actual override fun warnObj(obj: Any?) {
            NSLog("W/%s: %s", TAG_NAME, obj?.toString() ?: "null")
        }

        actual override fun warnThrowable(throwable: Throwable) {
            NSLog(
                "W/%s: %s (Throwable: %s)",
                TAG_NAME,
                throwable.message ?: "Error",
                throwable.toString()
            )
        }

        actual override fun error(message: String) {
            NSLog("E/%s: %s", TAG_NAME, message)
        }

        actual override fun errorObj(obj: Any?) {
            NSLog("E/%s: %s", TAG_NAME, obj?.toString() ?: "null")
        }

        actual override fun errorThrowable(throwable: Throwable) {
            NSLog(
                "E/%s: %s (Throwable: %s)",
                TAG_NAME,
                throwable.message ?: "Error",
                throwable.toString()
            )
        }

        actual override fun debug(message: String) {
            if (Platform.isDebugBinary) {
                NSLog("D/%s: %s", TAG_NAME, message)
            }
        }

        actual override fun debugObj(obj: Any?) {
            if (Platform.isDebugBinary) {
                NSLog("D/%s: %s", TAG_NAME, obj?.toString() ?: "null")
            }
        }

        actual override fun tag(tag: String): CanvasEggLogger {
            return newTagLogger(tag)
        }

        private fun newTagLogger(tagName: String): CanvasEggLogger {
            return object : CanvasEggLogger {
                // タグ付きロガーの各メソッドは、新しいタグを使用して NSLog を呼び出す
                override fun info(message: String) {
                    NSLog("I/%s: %s", tagName, message)
                }

                override fun infoObj(obj: Any?) {
                    NSLog("I/%s: %s", tagName, obj?.toString() ?: "null")
                }

                override fun warn(message: String) {
                    NSLog("W/%s: %s", tagName, message)
                }

                override fun warnObj(obj: Any?) {
                    NSLog("W/%s: %s", tagName, obj?.toString() ?: "null")
                }

                override fun warnThrowable(throwable: Throwable) {
                    NSLog(
                        "W/%s: %s (Throwable: %s)",
                        tagName,
                        throwable.message ?: "Error",
                        throwable.toString()
                    )
                }

                override fun error(message: String) {
                    NSLog("E/%s: %s", tagName, message)
                }

                override fun errorObj(obj: Any?) {
                    NSLog("E/%s: %s", tagName, obj?.toString() ?: "null")
                }

                override fun errorThrowable(throwable: Throwable) {
                    NSLog(
                        "E/%s: %s (Throwable: %s)",
                        tagName,
                        throwable.message ?: "Error",
                        throwable.toString()
                    )
                }

                override fun debug(message: String) {
                    NSLog("D/%s: %s", tagName, message)
                }

                override fun debugObj(obj: Any?) {
                    NSLog("D/%s: %s", tagName, obj?.toString() ?: "null")
                }

                override fun tag(tag: String): CanvasEggLogger {
                    return newTagLogger(tag) // 新しいタグのロガーを再帰的に生成
                }
            }
        }
    }
}