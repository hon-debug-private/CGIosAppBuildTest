package io.github.honatsugiexpress.canvasegg.script.engine

import io.github.honatsugiexpress.canvasegg.data.svg.resolver.script.SvgScriptEngine

expect sealed interface DefaultSvgScriptEngine: SvgScriptEngine {
    companion object {
        fun create(): DefaultSvgScriptEngine
    }
}