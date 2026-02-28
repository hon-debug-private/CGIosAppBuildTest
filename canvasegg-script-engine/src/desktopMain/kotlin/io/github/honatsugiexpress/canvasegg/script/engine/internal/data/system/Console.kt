package io.github.honatsugiexpress.canvasegg.script.engine.internal.data.system

import io.github.honatsugiexpress.canvasegg.common.log.CanvasEggLogger
import io.github.honatsugiexpress.canvasegg.script.engine.internal.util.JsProxyObject
import io.github.honatsugiexpress.canvasegg.script.engine.internal.util.ProxyVar
import org.graalvm.polyglot.Value

internal class Console: JsProxyObject() {
    override val members: List<ProxyVar> by lazy {
        listOf(
            _log
        )
    }
    private val logger = CanvasEggLogger.tag("JS Console")
    private val _log = ProxyVar(
        name = "log",
        get = ProxyVar.GetFunction.unit {
            log(*it)
        }
    )
    fun log(vararg args: Value?) {
        args.forEach { obj ->
            logger.info(obj.toString())
        }
    }
}