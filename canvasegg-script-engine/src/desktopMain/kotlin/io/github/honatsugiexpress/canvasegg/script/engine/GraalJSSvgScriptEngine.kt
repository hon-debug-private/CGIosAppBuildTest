package io.github.honatsugiexpress.canvasegg.script.engine

import io.github.honatsugiexpress.canvasegg.script.engine.internal.data.dom.Window
import io.github.honatsugiexpress.canvasegg.script.engine.internal.data.system.Console
import org.graalvm.polyglot.Context
import org.graalvm.polyglot.EnvironmentAccess
import org.graalvm.polyglot.HostAccess
import org.graalvm.polyglot.PolyglotAccess
import org.graalvm.polyglot.io.IOAccess

internal class GraalJSSvgScriptEngine: DefaultSvgScriptEngine {
    val context: Context = Context
        .newBuilder()
        .allowCreateThread(false)
        .allowCreateProcess(false)
        .allowEnvironmentAccess(EnvironmentAccess.NONE)
        .allowExperimentalOptions(false)
        .allowHostAccess(
            HostAccess
                .newBuilder()
                .allowAllClassImplementations(true)
                .allowAllImplementations(true)
                .allowAccessAnnotatedBy(HostAccess.Export::class.java)
                .allowImplementationsAnnotatedBy(HostAccess.Implementable::class.java)
                .allowImplementationsAnnotatedBy(FunctionalInterface::class.java)
                .build()
        )
        .allowHostClassLoading(false)
        .allowIO(IOAccess.NONE)
        .allowPolyglotAccess(PolyglotAccess.NONE)
        .allowNativeAccess(false)
        .useSystemExit(false)
        .build()
    override fun runScript(body: String) {
        context.eval("js", body)
    }

    init {
        val binding = context.getBindings("js")
        val window = Window()
        binding.putMember("window", window)
        binding.putMember("globalThis", window)
    }
}