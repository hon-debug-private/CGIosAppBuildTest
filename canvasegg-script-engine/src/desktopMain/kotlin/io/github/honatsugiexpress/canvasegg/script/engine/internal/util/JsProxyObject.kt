package io.github.honatsugiexpress.canvasegg.script.engine.internal.util

import org.graalvm.polyglot.Value
import org.graalvm.polyglot.proxy.ProxyObject

internal abstract class JsProxyObject: ProxyObject {
    abstract val members: List<ProxyVar>
    private fun filter(key: String?) = members.firstOrNull {
        it.name == key
    }
    final override fun getMember(key: String?): Any? {
        return when (val getter = filter(key)?.get) {
            is ProxyVar.Getter -> getter.block()
            is ProxyVar.GetFunction -> getter
            else -> null
        }
    }

    final override fun putMember(key: String?, value: Value?) {
        filter(key)?.set?.invoke(value)
    }

    final override fun hasMember(key: String?): Boolean {
        return members.firstNotNullOfOrNull {
            it.name == key
        } ?: false
    }

    final override fun getMemberKeys(): Any {
        return members
    }
}