package com.honatsugiexp.canvasegg.common.log

expect interface CanvasEggLogger {
    fun info(message: String)
    fun infoObj(obj: Any?)
    fun warn(message: String)
    fun warnObj(obj: Any?)
    fun error(message: String)
    fun errorObj(obj: Any?)
    fun errorThrowable(throwable: Throwable)
    fun debug(message: String)
    fun debugObj(obj: Any?)
    fun tag(tag: String): CanvasEggLogger

    companion object Default : CanvasEggLogger {
        override fun info(message: String)
        override fun infoObj(obj: Any?)

        override fun warn(message: String)
        override fun warnObj(obj: Any?)

        override fun error(message: String)
        override fun errorObj(obj: Any?)

        override fun errorThrowable(throwable: Throwable)

        override fun debug(message: String)
        override fun debugObj(obj: Any?)

        override fun tag(tag: String): CanvasEggLogger
    }
}