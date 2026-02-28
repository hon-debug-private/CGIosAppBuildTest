package io.github.honatsugiexpress.canvasegg.script.engine

import io.github.honatsugiexpress.canvasegg.data.svg.resolver.script.SvgScriptEngine

actual sealed interface DefaultSvgScriptEngine: SvgScriptEngine {
    actual companion object {
        actual fun create(): DefaultSvgScriptEngine {
            return GraalJSSvgScriptEngine()
        }
    }
}