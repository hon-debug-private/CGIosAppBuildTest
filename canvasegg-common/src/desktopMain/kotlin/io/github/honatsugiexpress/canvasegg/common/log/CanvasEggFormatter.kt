package io.github.honatsugiexpress.canvasegg.common.log

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.logging.Formatter
import java.util.logging.Level
import java.util.logging.LogRecord

class CanvasEggFormatter: Formatter() {
    override fun format(record: LogRecord?): String {
        return record?.let {
            val color: String?
            val level: Level? = record.level

            color = when (level) {
                Level.SEVERE -> {
                    ANSI_RED
                }

                Level.WARNING -> {
                    ANSI_YELLOW
                }

                Level.INFO -> {
                    ANSI_CYAN
                }

                Level.CONFIG -> {
                    ANSI_GREEN
                }

                else -> {
                    ANSI_RESET
                }
            }
            val result = buildString {
                append(ANSI_WHITE)
                append(DATE_FORMAT.format(Date(it.millis)))
                append(" ")
                append(color)
                append("[")
                append(it.level.name)
                append(", ")
                append(it.loggerName)
                append("]: ")
                append(it.message)
                append(ANSI_RESET)
                append("\n")
            }
            result
        } ?: ""
    }
    companion object {
        val DATE_FORMAT: SimpleDateFormat =
            SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS", Locale.ENGLISH)
        private const val ANSI_RESET = "\u001B[0m"
        private const val ANSI_RED = "\u001B[31m"
        private const val ANSI_YELLOW = "\u001B[33m"
        private const val ANSI_CYAN = "\u001B[36m"
        private const val ANSI_WHITE = "\u001B[37m"
        private const val ANSI_GREEN: String = "\u001B[32m"
    }
}