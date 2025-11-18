package com.honatsugiexp.canvasegg.common.log

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.logging.Formatter
import java.util.logging.LogRecord

class CanvasEggFormatter: Formatter() {
    override fun format(record: LogRecord?): String {
        return record?.let {
            "${DATE_FORMAT.format(Date(record.millis))} [${record.level.name}, ${record.loggerName}]: ${record.message}"
        } ?: ""
    }
    companion object {
        val DATE_FORMAT: SimpleDateFormat =
            SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS", Locale.ENGLISH)
    }
}