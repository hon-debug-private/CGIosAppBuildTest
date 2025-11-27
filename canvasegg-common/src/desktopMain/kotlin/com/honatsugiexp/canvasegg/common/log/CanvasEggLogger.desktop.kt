package com.honatsugiexp.canvasegg.common.log

import java.util.logging.ConsoleHandler
import java.util.logging.Level
import java.util.logging.Logger as JLogger


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
        const val TAG_NAME = "CanvasEgg"
        val logger: JLogger = JLogger.getLogger(TAG_NAME).apply {
            level = Level.INFO
            useParentHandlers = false

            val handler = ConsoleHandler()
            handler.formatter = CanvasEggFormatter()
            addHandler(handler)
        }
        actual override fun info(message: String) {
            logger.info(message)
        }

        actual override fun infoObj(obj: Any?) {
            logger.info(obj.toString())
        }

        actual override fun warn(message: String) {
            logger.warning(message)
        }

        actual override fun warnObj(obj: Any?) {
            logger.warning(obj.toString())
        }

        actual override fun warnThrowable(throwable: Throwable) {
            logger.warning(throwable.stackTraceToString())
        }

        actual override fun error(message: String) {
            logger.severe(message)
        }

        actual override fun errorObj(obj: Any?) {
            logger.severe(obj.toString())
        }

        actual override fun errorThrowable(throwable: Throwable) {
            logger.severe(throwable.stackTraceToString())
        }

        actual override fun debug(message: String) {
            logger.fine(message)
        }

        actual override fun debugObj(obj: Any?) {
            logger.fine(obj.toString())
        }

        actual override fun tag(tag: String): CanvasEggLogger {
            return newTagLogger(tag)
        }
        private fun newTagLogger(tagName: String): CanvasEggLogger {
            return object : CanvasEggLogger {
                val logger: JLogger = JLogger.getLogger(tagName).apply {
                    level = Level.INFO
                    useParentHandlers = false

                    val handler = ConsoleHandler()
                    handler.formatter = CanvasEggFormatter()
                    addHandler(handler)
                }
                override fun info(message: String) {
                    logger.info(message)
                }

                override fun infoObj(obj: Any?) {
                    logger.info(obj.toString())
                }

                override fun warn(message: String) {
                    logger.warning(message)
                }

                override fun warnObj(obj: Any?) {
                    logger.warning(obj.toString())
                }

                override fun warnThrowable(throwable: Throwable) {
                    logger.warning(throwable.stackTraceToString())
                }

                override fun error(message: String) {
                    logger.severe(message)
                }

                override fun errorObj(obj: Any?) {
                    logger.severe(obj.toString())
                }

                override fun errorThrowable(throwable: Throwable) {
                    logger.severe(throwable.stackTraceToString())
                }

                override fun debug(message: String) {
                    logger.fine(message)
                }

                override fun debugObj(obj: Any?) {
                    logger.fine(obj.toString())
                }

                override fun tag(tag: String): CanvasEggLogger {
                    return newTagLogger(tag)
                }
            }
        }
    }
}