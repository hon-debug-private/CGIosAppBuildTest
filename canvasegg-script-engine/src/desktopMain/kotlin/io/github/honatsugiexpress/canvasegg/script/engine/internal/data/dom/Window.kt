package io.github.honatsugiexpress.canvasegg.script.engine.internal.data.dom

import io.github.honatsugiexpress.canvasegg.script.engine.internal.data.system.Console
import io.github.honatsugiexpress.canvasegg.script.engine.internal.util.JsProxyObject
import io.github.honatsugiexpress.canvasegg.script.engine.internal.util.ProxyVar
import kotlin.random.Random

internal class Window: JsProxyObject() {
    override val members: List<ProxyVar> by lazy {
        listOf(
            console
        )
    }
    private val consoleInstance = Console()
    val console = ProxyVar(
        name = "console",
        get = ProxyVar.GetterValue { consoleInstance }
    )
}