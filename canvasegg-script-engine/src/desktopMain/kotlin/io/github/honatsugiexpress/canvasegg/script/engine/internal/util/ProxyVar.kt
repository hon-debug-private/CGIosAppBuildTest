package io.github.honatsugiexpress.canvasegg.script.engine.internal.util

import org.graalvm.polyglot.Value
import org.graalvm.polyglot.proxy.ProxyExecutable

internal data class ProxyVar(
    val name: String,
    val get: GetValue = GetNone,
    val set: ((Value?) -> Unit)? = null
) {
    sealed interface GetValue
    sealed class Getter(val block: () -> Value?): GetValue
    class GetterValue(block: () -> Any?): Getter({
        Value.asValue(block())
    })
    class GetFunction(val block: (array: Array<out Value?>) -> Value?): GetValue, ProxyExecutable {
        override fun execute(vararg arguments: Value?): Any? {
            return block(arguments)
        }
        companion object {
            fun unit(block: (array: Array<out Value?>) -> Unit) = GetFunction {
                block(it)
                Value.asValue(null)
            }
        }
    }
    object GetNone: GetValue
}